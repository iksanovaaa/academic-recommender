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

class IndexView extends View {
    constructor() {
        super();

        this.colorizedTextBlock = document.getElementById('colorized-html');
        this.uploadedFile = () => {};

        this.corpusList = new ItemList('corpus');
        this.documentList = new ItemList('document');
        this.annotationList = new ItemList('annotation');
        this.markerList = new ItemList('marker');
        this.annotationList.add('IPronoun');

        this.currentCorpus = -1;
        this.currentDocument = -1;

        this.corpora = [];
        this.documents = [];
        this.annotations = [];
        this.markers = [];

        this.fetchCorpora();

        /* this.corpusList.add('Corpus 1');
        this.documentList.addRange(['Document 1', 'Document 2', 'Document 3']);
        this.annotationList.addRange(['Annotation 1', 'Annotation 2', 'Annotation 3']);
        this.markerList.addRange(['KOODA', 'GOTTI', 'BILLY']); */

        // var dropZone = document.getElementById('document-drop-zone');

        // A moment when a file is over the drag-and-drop zone
        /* dropZone.addEventListener('dragover', evt => {
            evt.stopPropagation();
            evt.preventDefault();
            evt.dataTransfer.dropEffect = 'copy';
        }, false);
        
        // When a user actually drops the file
        dropZone.addEventListener('drop', evt => {
            evt.stopPropagation();
            evt.preventDefault();

            dropZone.innerText = 'File is loaded';
            this.uploadedFile = evt.dataTransfer.files[0];
        }, false); */
    }

    set colorizedHtml(value = '') {
        this.colorizedTextBlock.innerHTML = value;
    }

    setAnnotation(index, value) {
        const className = 'color-' + (index + 1);
        const disabledClassName = className + '-disabled';

        const elements = [].slice.call(
            document.getElementsByClassName(value ? disabledClassName : className)
        );
        
        elements.forEach(function(item, i, arr) {
            if (value) {
                item.classList.add(className);
                item.classList.remove(disabledClassName);
            } else {
                item.classList.add(disabledClassName);
                item.classList.remove(className);
            }
        });
    }

    disableAll() {
        const controllers = document.getElementsByClassName('controller');

        for (let i = 0; i < controllers.length; ++i) {
            controllers[i].checked = false;
            this.setAnnotation(i, false);
        }
    }

    createCorpus() {
        postRequest(
            'api/create_corpus', 
            { name: document.getElementById('corpus-name').value }
        ).then(
            () => this.fetchCorpora(),
            err => alert('Unable to create a corpus: ' + err.message)
        );
    }

    createDocument() {
        const dropZone = new Optional(document.getElementById('document-drop-zone'));

        dropZone.map(
            zone => zone.files[0]
        ).ifPresent(
            file => {
                const reader = new FileReader();

                reader.onload = (e) => {
                    const documentInfo = { 
                        corpusId: this.corpora[this.currentCorpus].id,
                        name: document.getElementById('document-name').value,
                        content: e.target.result
                    };

                    postRequest('api/create_document', documentInfo).then(
                        () => this.fetchDocuments(this.currentCorpus)
                    ).catch(
                        err => alert('Unable to create a document: ' + err.message)
                    );
                };

                reader.readAsText(file);
            }
        );

        /* const dropZone = document.getElementById('document-drop-zone');

        if (dropZone) {
            const file = dropZone.files[0];

            if (file) {
                const reader = new FileReader();

                reader.onload = (e) => {
                    const documentInfo = { 
                        corpusId: this.corpora[this.currentCorpus].id,
                        name: document.getElementById('document-name').value,
                        content: e.target.result
                    };

                    postRequest('api/create_document', documentInfo).then(
                        () => this.fetchDocuments(this.currentCorpus)
                    ).catch(
                        err => alert('Unable to create a document: ' + err.message)
                    );
                };

                reader.readAsText(file);
            }
        } */
    }

    fetchCorpora() {
        this.startLoading('corpora')

        getRequest('api/read_corpus').then(
            reply => {
                this.updateLists('corpora', 'corpusList', reply).forEach(
                    (cur, idx) => {
                        // Set up the corpus list item
                        cur.extras.clickable[0].onclick = () => {
                            this.currentCorpus = idx;
                            this.fetchDocuments(this.currentCorpus);
                        };

                        // Set up the corpus deleter
                        cur.extras.deleter[0].onclick = () => {
                            this.deleteCorpus(idx);
                        };
                    }
                );
            },
            err => alert('Failed to fetch corpora: ' + err.message) 
        ).finally(
            () => this.finishLoading('corpora')
        );
    }

    fetchDocuments(corpusIdx) {
        this.startLoading('documents')

        getRequest(
            'api/read_document',
            { corpus: this.corpora[corpusIdx].id }
        ).then(
            reply => {
                this.updateLists('documents', 'documentList', reply).forEach(
                    (cur, idx) => {
                        // Set up the document list item
                        cur.extras.clickable[0].onclick = event => {
                            this.currentDocument = idx;
                            this.fetchText(this.currentDocument);
                        };

                        cur.extras.deleter[0].onclick = event => {
                            this.deleteDocument(idx);
                        };
                    }
                );
            },
            err => alert('Failed to fetch documents: ' + err.message) 
        ).finally(
            () => this.finishLoading('documents')
        );
    }

    fetchText(documentIdx) {
        this.startLoading('text')

        getRequest(
            'api/read_text',
            { document: this.documents[documentIdx].id }
        ).then(
            reply => {
                const root = document.getElementById('colorized-html');

                root.innerHTML = reply.text;

                this.updateLists('annotations', 'annotationList', reply.annotations).forEach(
                    (cur, idx) => {
                        cur.extras.content[0].classList.add(`color-${idx + 1}`);

                        const checkbox = cur.extras.clickable[0];
                        checkbox.checked = true;
                        checkbox.onchange = () => this.setAnnotation(idx, checkbox.checked);
                    }
                );

                this.updateLists('markers', 'markerList', reply.annotationList).forEach(
                    (cur, idx) => {
                        const contents = cur.extras.content;
                        const marker = reply.annotationList[idx];

                        contents[0].classList.add(marker.type);
                        contents[1].textContent = marker.startNode;
                        contents[2].textContent = marker.endNode;
                    }
                );
            },
            err => alert('Failed to fetch the document text: ' + err.message) 
        ).finally(
            () => this.finishLoading('text')
        );
    }

    deleteCorpus(corpusIdx) {
        postRequest(
            'api/delete_corpus',
            this.corpora[corpusIdx]
        ).then(
            reply => {
                this.corpora.splice(corpusIdx, 1);
                this.corpusList.remove(corpusIdx);

                // ToDo: can be optimized (performed locally)
                //this.fetchCorpora();
            }
        );
    }

    deleteDocument(documentIdx) {
        postRequest(
            'api/delete_document',
            this.documents[documentIdx]
        ).then(
            reply => {
                this.documents.splice(documentIdx, 1);
                this.documentList.remove(documentIdx);

                // ToDo: can be optimized (performed locally)
                // this.fetchDocuments();
            }
        );
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
        loading.classList.remove('hidden');
    }

    finishLoading(listName) {
        const loading = document.getElementById(listName + '-loading');
        loading.classList.add('hidden');
    }
}
