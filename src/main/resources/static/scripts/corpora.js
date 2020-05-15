class CorporaView extends View {
    constructor() {
        super();

        // Set up corpus element list and corpus object list
        this.corpusList = new ItemList('corpus');
        this.corpora = [];

        // Fetch objects from the server
        this.fetchCorpora();

        // Set up the modal window
        this._corpusModal = new Modal('corpus-modal');

        // Set up modal onSubmit property to create a new corpus
        this._corpusModal.onSubmit = (form) => this.createCorpus(form);

        // Show the modal by clicking the button
        document.querySelector(
            '#new-corpus'
        ).onclick = () => this._corpusModal.show();
    }

    createCorpus(form) {
        postRequest(
            'api/create_corpus', 
            //{ name: form.name }
            { name: document.getElementById('corpus-name').value }
        ).then(
            () => this.fetchCorpora(),
            err => alert('Unable to create a corpus: ' + err.message)
        );
    }

    fetchCorpora() {
        this.startLoading('corpora')

        getRequest('api/read_corpus').then(
            reply => {
                this.updateLists('corpora', 'corpusList', reply).forEach(
                    (cur, idx) => {
                        // Set up the corpus list item
                        /* cur.extras.clickable[0].onclick = () => {
                            this.currentCorpus = idx;
                            this.fetchDocuments(this.currentCorpus);
                        }; */
                        cur.extras.clickable[0].href = '/documents' + concatAndEncode(this.corpora[idx]);

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

    deleteCorpus(corpusIdx) {
        postRequest(
            'api/delete_corpus',
            this.corpora[corpusIdx]
        ).then(
            reply => {
                this.corpora.splice(corpusIdx, 1);
                this.corpusList.remove(corpusIdx);

                // Performed locally
                //this.fetchCorpora();
            }
        );
    }
}