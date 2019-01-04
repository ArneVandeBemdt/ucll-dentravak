export default class API {
    static constructApiUrl(endpoint) {
        if (endpoint.charAt(0) === '/') {
            endpoint = endpoint.substr(1);
        }
        // return "http://localhost:8080/den-travak/" + endpoint;
	return "http://193.191.177.8:10438/den-travak/" + endpoint;
    }
}
