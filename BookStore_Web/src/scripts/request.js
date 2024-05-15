const BASE_URL = "http://localhost:3000/api";

export async function sendRequest(url, method, data) {
    let headers = "";
    let token = localStorage.getItem("Token");
    headers = token? "Bearer " + token : "";

    const request = {
        method: method, 
        headers: {
            'Content-Type': 'application/json',
            'Authorization': headers
        }
    }

    if (data) request.body = JSON.stringify(data);

    const response = await fetch(BASE_URL + url, request);
    if (response.status === 401) {
        localStorage.clear;
    }

    return response
}
