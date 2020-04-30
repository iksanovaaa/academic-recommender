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
                if (cur.classList.contains(x) && !acc[x]) {
                    acc[x] = cur;
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

        if (templateMap.content) {
            templateMap.content.textContent = content;
        }

        //this.setContent(template, content);

        template.id = ''; // this.baseId + '-' + (this.element.children.length + 1).toString();
        //template.classList.remove('hidden');

        // Add some extra info
        template.extras = {
            clickable: templateMap.clickable,
            deleter: templateMap.deleter,
            index: this.length
        };

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
        this.annotationList.add('IPronoun');

        this.currentCorpus = -1;
        this.currentDocument = -1;

        this.corpora = [];
        this.documents = [];
        this.annotations = [];

        this.fetchCorpora();

        this.corpusList.add('Corpus 1');
        this.documentList.addRange(['Document 1', 'Document 2', 'Document 3']);
        this.annotationList.addRange(['Annotation 1', 'Annotation 2', 'Annotation 3']);

        var dropZone = document.getElementById('document-drop-zone');
        dropZone.addEventListener('dragover', evt => this.handleDragOver(evt), false);
        dropZone.addEventListener('drop', evt => this.handleFileSelect(evt), false);
    }

    set colorizedHtml(value = '') {
        this.colorizedTextBlock.innerHTML = value;
    }

    handleFileSelect(evt) {
        evt.stopPropagation();
        evt.preventDefault();

        const file = evt.dataTransfer.files[0];

        this.uploadedFile = () => {
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
        };
    }

    handleDragOver(evt) {
        evt.stopPropagation();
        evt.preventDefault();
        evt.dataTransfer.dropEffect = 'copy'; // Explicitly show this is a copy.
    }

    enableAnnotations(index) {
        const className = 'color-' + (index + 1);
        const disabledClassName = className + '-disabled';

        const elements = [].slice.call(document.getElementsByClassName(disabledClassName));
        
        elements.forEach(function(item, i, arr) {
            item.classList.add(className);
            item.classList.remove(disabledClassName);
        });
    }

    disableAnnotations(index) {
        const className = 'color-' + (index + 1);
        const disabledClassName = className + '-disabled';

        const elements = [].slice.call(document.getElementsByClassName(className));
        
        elements.forEach(function(item, i, arr) {
            item.classList.add(disabledClassName);
            item.classList.remove(className);
        });
    }

    disableAll() {
        const controllers = document.getElementsByClassName('controller');

        for (let i = 0; i < controllers.length; ++i) {
            controllers[i].checked = false;
            this.disableAnnotations(i);
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
        this.uploadedFile.call(this);
    }

    fetchCorpora() {
        this.startLoading('corpora')

        getRequest('api/read_corpus').then(
            reply => {
                this.updateLists('corpora', 'corpusList', reply).forEach(
                    (cur, idx) => {
                        // Set up the corpus list item
                        cur.extras.clickable.onclick = event => {
                            this.currentCorpus = idx;
                            this.fetchDocuments(this.currentCorpus);
                        };

                        // Set up the corpus deleter
                        cur.extras.deleter.onclick = event => {
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
                        cur.extras.clickable.onclick = event => {
                            this.currentDocument = idx;
                            this.fetchText(this.currentDocument);
                        };

                        cur.extras.deleter.onclick = event => {
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
                        const checkbox = cur.extras.clickable;
                        checkbox.checked = true;

                        checkbox.onchange = event => {
                            if (checkbox.checked) {
                                this.enableAnnotations(idx);
                            } else {
                                this.disableAnnotations(idx);
                            }
                        };
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
            this[cache].map((cur, idx, self) => `${idx}: ${cur.name}`)
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
