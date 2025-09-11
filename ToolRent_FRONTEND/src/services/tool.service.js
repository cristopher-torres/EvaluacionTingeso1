import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/api/tools/getTools');
}

export default { getAll};