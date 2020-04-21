function concatAndEncode(data) {
    var parameters = [];

    for (var field in data) {
        if (Object.prototype.hasOwnProperty.call(data, field)) {
            parameters.push(encodeURI(field + '=' + data[field]));
        }
    }

    return ((parameters.length > 0) ? '?' : '') + parameters.join('&');
}

function makeRequest(url, method, ok, fail) {
    var xhr = new XMLHttpRequest();
    xhr.open(method, url);

    xhr.onload = function() {
        if (xhr.status === 200 && ok) {
            ok(xhr.response);
        } else if (fail) {
            fail();
        }
    };

    return xhr;
}

function getRequest(url, data, ok, fail) {
    var xhr = makeRequest(url /*+ concatAndEncode(data)*/, 'GET', ok, fail);
    
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify(data));
}

function postRequest(url, data, ok, fail) {
    var xhr = makeRequest(url, 'POST', ok, fail);
    
    xhr.setRequestHeader('Content-Type', 'application/json'); // 'application/x-www-form-urlencoded');
    xhr.send(JSON.stringify(data)); //concatAndEncode(data));
}

function ajaxRequest(url, data, ok, fail) {
    var xhr = makeRequest(url, 'PUT', function(response) { 
        ok(JSON.parse(response.responseText)); 
    }, fail);

    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify(data));
}

function registerModal(mainContent, modalBody, openButton) {
    const content = document.getElementById(mainContent);
    const modal = document.getElementById(modalBody);
    const btn = document.getElementById(openButton);
    const span = document.getElementById(modalBody + '-close');

    function closeModal() {
        modal.style.display = "none";
        content.classList.remove('blurred');
    }

    // When the user clicks on the button, open the modal
    btn.onclick = () => {
        modal.style.display = "block";
        content.classList.add('blurred');
    }

    // When the user clicks on <span> (x), close the modal
    span.onclick = () => {
        closeModal();
    }

    // When the user clicks anywhere outside of the modal, close it
    window.onclick = (event) => {
        if (event.target == modal) {
            closeModal();
        }
    }
}