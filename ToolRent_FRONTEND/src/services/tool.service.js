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

const update = (tool) => {
  return httpClient.put(`api/tools/updateTool/${tool.id}`, tool);
};

const get = (id) => {
  return httpClient.get(`api/tools/getTool/${id}`);
}

const getAvailable = () => {
  return httpClient.get('/api/tools/available');
}  

export default { getAll, create, getStock, update, get, getAvailable };