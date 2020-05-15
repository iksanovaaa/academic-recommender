function concatAndEncode(data) {
    var parameters = [];

    for (var field in data) {
        if (Object.prototype.hasOwnProperty.call(data, field)) {
            parameters.push(encodeURI(field + '=' + data[field]));
        }
    }

    return ((parameters.length > 0) ? '?' : '') + parameters.join('&');
}

function createEncoderFromString(format) {
    switch (format) {
        case 'json': 
            return new class {
                params() { return ''; }
                encode(data) { return JSON.stringify(data); }
                decode(data) { return JSON.parse(data ? data : '{}'); }
                get header() { return 'application/json'; }
            };

        case 'plain': 
            return new class {
                params() { return ''; }
                encode(data) { return data; }
                decode(data) { return data; }
                get header() { return 'text/plain'; }
            }

        case 'urlencoded':
            return new class {
                params(data) { return concatAndEncode(data); }
                encode() { return ''; }
                decode() { return ''; }
                get header() { return 'application/x-www-form-urlencoded'; }
            }

        default: throw new Error(`Format ${format} is not supported`);
    }
}

function defaultValue(object, key, value) {
    if (!object[key]) {
        object[key] = value;
    }
}

function makeRequest(method, url, data, settings = {}) {
    const DefaultFormat = 'json';
    
    return new Promise((resolve, reject) => {
        try {
            const format = settings.format || { send: DefaultFormat, receive: DefaultFormat };
            const sendEncoder = createEncoderFromString(format.send || DefaultFormat);
            const receiveEncoder = createEncoderFromString(format.receive || DefaultFormat);
                
            const xhr = new XMLHttpRequest();
            xhr.open(method, url + sendEncoder.params(data));
        
            xhr.onload = () => {
                if (xhr.status === 200) {
                    resolve(receiveEncoder.decode(xhr.response));
                } else {
                    reject(new Error('Failed to fulfill the request: status ' + xhr.status));
                }
            };
        
            xhr.setRequestHeader('Content-Type', sendEncoder.header);
            xhr.send(sendEncoder.encode(data));
        } catch (err) {
            reject(err);
        }
    });
}

function getRequest(url, data = {}, settings = {}) {
    defaultValue(settings, 'format', {});
    settings.format.send = 'urlencoded';

    return makeRequest('GET', url, data, settings);
}

function postRequest(url, data = {}, settings = {}) {
    return makeRequest('POST', url, data, settings);
}

function ajaxRequest(url, data = {}, settings = {}) {
    return makeRequest('PUT', url, data, settings);
}

class MissingValueError extends Error {
    constructor(message, cause) {
        super(message);

        this.cause = cause;
        this.name = 'MissingValueError';
        this.stack = cause.stack;
    }
}

const nullObject = {};

class Optional {
    constructor(value) {
        if (value === undefined || value === null) {
            value = nullObject;
        }

        this._value = value;
    }

    get value() {
        if (this.present) {
            return this._value;
        } else {
            throw new MissingValueError('No value set for the Optional instance');
        }
    }

    get present() {
        return this._value !== nullObject;
    }

    static of(value) {
        return new Optional(value);
    }

    toString() {
        return this.map(value => value).orElse('<empty>');
    }

    generateMap(resolve, reject = () => new Optional()) {
        try {
            return resolve();
        } catch (err) {
            if (err.name = 'MissingValueError') {
                return reject();
            } else {
                throw err;
            }
        }
    }

    filter(predicate) {
        return this.generateMap(() => {
            if (predicate(this.value)) {
                return new Optional(this.value)
            } else {
                return new Optional();
            }
        });
    }

    map(mapper) {
        return this.generateMap(() => new Optional(mapper(this.value)));
    }

    flatMap(mapper) {
        return this.generateMap(() => mapper(this.value));
    }

    ifPresent(consumer) {
        this.generateMap(() => consumer(this.value));
    }

    orElse(other) {
        this.orElseGet(() => other);
    }

    orElseGet(supplier) {
        this.generateMap(() => this.value, supplier);
    }
}

function isFunction(functionToCheck) {
    return functionToCheck && {}.toString.call(functionToCheck) === '[object Function]';
}

function isString(object) {
    return typeof object === 'string';
}

function traverse(root) {
    const stack = [root];
    const acc = {
        classes: {},
        ids: {},
        names: {}
    };

    while (stack.length > 0) {
        const cur = stack.pop();

        [...cur.classList].forEach(
            name => {
                const place = acc.classes;

                if (place[name]) {
                    place[name].push(cur);
                } else {
                    place[name] = [cur];
                }
            }
        );

        if (cur.id) {
            acc.ids[cur.id] = cur;
        }

        if (cur.name && isString(cur.name)) {
            acc.names[cur.name] = cur;
        }

        [...cur.children].forEach(
            child => stack.push(child)
        );
    }

    return acc;
}

function setClass(element, className, on) {
    if (on) {
        element.classList.add(className);
    } else {
        element.classList.remove(className);
    }
}

class Modal {
    constructor(rootId) {
        this._root = document.getElementById(rootId);
        this._tree = traverse(this._root);

        this._tree.classes.close.forEach(x => x.onclick = () => this.hide());
        this._tree.classes.submit.forEach(x => x.onclick = () => this.submit());

        this._backgroundClick = event => {
            if (event.target === this._root) {
                this.hide();
            }
        };

        this._onSubmit = new Optional();
    }
    
    set onSubmit(value) {
        if (!isFunction(value)) {
            throw new Error('Not a function');
        }

        this._onSubmit = new Optional(value);
    }

    show() {
        this._root.hidden = false;
        window.addEventListener('click', this._backgroundClick);
    }

    hide() {
        this._root.hidden = true;
        window.removeEventListener('click', this._backgroundClick);
    }

    submit() {
        const form = {};

        Object.getOwnPropertyNames(this._tree.names).forEach(
            key => form[key] = this._tree.names[key].value
        );

        try {
            this._onSubmit.ifPresent(callback => callback(form));
        } catch (e) {
            throw e;
        } finally {
            this.hide();
        }
    }
}

class ItemList {
    constructor(id) {
        this.baseId = id;
        this.element = document.getElementById(id + '-list');
        this._items = [];
    }

    get items() {
        return this._items;
    }

    get length() {
        return this._items.length;
    }

    add(childContent) {
        const template = this.copyAndSetupTemplate(childContent);

        this.element.appendChild(template);

        // Save in an internal array of elements
        this._items.push(template);

        return template;
    }

    addRange(range) {
        return range.map(x => this.add(x));
    }

    remove(index) {
        if (index >= 0 && index < this._items.length) {
            this.element.removeChild(this._items[index]);
            this._items.splice(index, 1);
        }
    }

    clear() {
        this._items.forEach(x => this.element.removeChild(x));
        this._items = [];
    }

    setContent(node, content) {
        if (node.classList.contains('content')) {
            node.classList.remove('content');
            node.textContent = content;
            return;
        }

        const children = [...node.children];
        children.forEach(x => this.setContent(x, content));
    }

    traverse(cur, acc = {}) {
        ['clickable', 'content', 'deleter'].forEach(
            x => {
                if (cur.classList.contains(x)) {
                    if (!acc[x]) {
                        acc[x] = []
                    }

                    acc[x].push(cur);
                    cur.classList.remove(x);
                }
            }
        );

        [...cur.children].forEach(x => this.traverse(x, acc));

        return acc;
    }

    copyAndSetupTemplate(content) {
        const template = document.getElementById(this.baseId + '-template').cloneNode(true);
        
        const templateMap = this.traverse(template);

        template.id = '';

        // Add some extra info
        template.extras = {
            clickable: templateMap.clickable,
            content: templateMap.content,
            deleter: templateMap.deleter,
            index: this.length
        };

        const extras = template.extras;

        if (extras.content) {
            if (Array.isArray(content)) {
                extras.content.forEach(
                    (node, idx) => node.textContent = content[idx]
                );
            } else {
                extras.content[0].textContent = content;
            }
        }

        return template;
    }
}

class View {
    constructor() {
        
    }

    updateLists(cache, list, newValues) {
        // Save the documents of the corpus in the field
        this[cache] = newValues;

        this[list].clear();
        
        return this[list].addRange(
            this[cache].map(cur => `${cur.name}`)
        );
    }
    
    startLoading(listName) {
        const loading = document.getElementById(listName + '-loading');
        loading.hidden = false;
    }

    finishLoading(listName) {
        const loading = document.getElementById(listName + '-loading');
        loading.hidden = true;
    }

    // registerModal(mainContent, modalBody, openButton) {
    //     const content = document.getElementById(mainContent);
    //     const modal = document.getElementById(modalBody);
    //     const btn = document.getElementById(openButton);
    //     const span = document.getElementById(modalBody + '-close');
    
    //     function openModal() {
    //         modal.hidden = false;
    //         //modal.classList.remove('hidden');
    //         content.classList.add('blurred');
    //     }

    //     function closeModal() {
    //         modal.hidden = true;
    //         //modal.classList.add('hidden');
    //         content.classList.remove('blurred');
    //     }

    //     btn.onclick = openModal;
    //     span.onclick = closeModal;

    //     const oldHandler = new Optional(window.onclick);

    //     // When the user clicks anywhere outside of the modal, close it
    //     window.onclick = (event) => {
    //         if (event.target == modal) {
    //             closeModal();
    //         }

    //         oldHandler.ifPresent((handler) => handler(event));

    //         /* if (oldHandler) {
    //             oldHandler(event);
    //         } */
    //     };
    // }
}
