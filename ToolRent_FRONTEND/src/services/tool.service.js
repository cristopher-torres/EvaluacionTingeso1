import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/api/tools/getTools');
}

const create = (data, quantity) => {
  return httpClient.post(`/api/tools/createTool/${quantity}`, data);
};

const getStock = () => {
    return httpClient.get('/api/tools/stock');
}

export default { getAll, create, getStock };