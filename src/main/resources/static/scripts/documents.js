class DocumentsView extends View {
    constructor(corpus) {
        super();

        this.colorizedTextBlock = document.getElementById('colorized-html');

        this.documentList = new ItemList('document');
        this.annotationList = new ItemList('annotation');
        this.markerList = new ItemList('marker');

        this.currentDocument = -1;

        this._corpus = corpus;
        this.documents = [];
        this.annotations = [];
        this.markers = [];

        this.fetchDocuments();

        this._documentModal = new Modal('document-modal');

        this._documentModal.onSubmit = (form) => this.createDocument(form);

        document.querySelector(
            '#new-document'
        ).onclick = () => this._documentModal.show();
    }

    set colorizedHtml(value = '') {
        this.colorizedTextBlock.innerHTML = value;
    }

    setAnnotation(index, value) {
        const className = 'color-' + (index + 1);
        this.setAnnotationByType(className, value);
    }

    setAnnotationByType(className, value) {
        const disabledClassName = className + '-disabled';

        const elements = [].slice.call(
            document.getElementsByClassName(value ? disabledClassName : className)
        );
        
        elements.forEach(function(item, i, arr) {
            if (value) {
                item.classList.add(className);
                item.classList.remove(disabledClassName);
                if (item.classList.contains('marker')) item.parentElement.hidden = false;
            } else {
                item.classList.add(disabledClassName);
                item.classList.remove(className);
                if (item.classList.contains('marker')) item.parentElement.hidden = true;
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

    createDocument(form) {
        const dropZone = document.getElementById('document-drop-zone');

        const file = new Optional(dropZone.files[0]);

        file.ifPresent(
            file => {
                const reader = new FileReader();

                reader.onload = (e) => {
                    const documentInfo = { 
                        corpusId: this._corpus.id /* this.corpora[this.currentCorpus].id */,
                        name: document.getElementById('document-name').value,
                        content: e.target.result
                    };

                    postRequest('api/create_document', documentInfo).then(
                        () => this.fetchDocuments()
                    ).catch(
                        err => alert('Unable to create a document: ' + err.message)
                    );
                };

                reader.readAsText(file);
            }
        );
    }

    createDocuments(form) {
        const dropZone = document.getElementById('document-drop-zone');
        const promises = [...dropZone.files].map(
            file => (async () => ({
                    corpusId: this._corpus.id,
                    name: file.name,
                    content: await file.text()
                }))()
        );

        Promise.all(promises).then(
            contents => postRequest('api/create_documents', contents).then(
                    () => this.fetchDocuments()
                )
        ).catch(
            err => alert('Unable to create documents: ' + err.message)
        );
    }

    fetchDocuments() {
        this.startLoading('documents');

        getRequest(
            'api/read_document',
            { corpusId: this._corpus.id}
            // this.corpora[corpusIdx] 
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
            this.documents[documentIdx]
            // { document: this.documents[documentIdx].id }
        ).then(
            reply => {
                const root = document.getElementById('colorized-html');

                root.innerHTML = reply.text;

                this.updateLists('annotations', 'annotationList', reply.annotations).forEach(
                    (cur, idx) => {
                        const type = this.annotations[idx].type;
                        cur.extras.content[0].classList.add(type);

                        const checkbox = cur.extras.clickable[0];
                        checkbox.checked = true;
                        checkbox.onchange = () => this.setAnnotationByType(type, checkbox.checked);
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

    deleteDocument(documentIdx) {
        postRequest(
            'api/delete_document',
            this.documents[documentIdx]
        ).then(
            reply => {
                this.documents.splice(documentIdx, 1);
                this.documentList.remove(documentIdx);

                // Performed locally
                // this.fetchDocuments();
            }
        );
    }
}
