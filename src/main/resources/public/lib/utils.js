const htmlToElement = (html) => {
    var template = document.createElement('template');
    html = html.trim(); // Never return a text node of whitespace as the result
    template.innerHTML = html;
    return template.content.firstChild;
}

export default function constructApiUrl(endpoint) {
    if (endpoint.charAt(0) === '/') {
        endpoint = endpoint.substr(1);
    }
    return "http://localhost:8080/den-travak/" + endpoint;
}