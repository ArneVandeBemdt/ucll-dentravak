export default class API {
    static constructApiUrl(endpoint) {
        if (endpoint.charAt(0) === '/') {
            endpoint = endpoint.substr(1);
        }
        return "http://localhost:8080/den-travak/" + endpoint;
    }
}