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
    makeRequest(url + concatAndEncode(data), 'GET', ok, fail).send();
}

function postRequest(url, data, ok, fail) {
    var xhr = makeRequest(url, 'POST', ok, fail);
    
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.send(concatAndEncode(data));
}

function ajaxRequest(url, data, ok, fail) {
    var xhr = makeRequest(url, 'PUT', function(response) { 
        ok(JSON.parse(response.responseText)); 
    }, fail);

    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify(data));
}