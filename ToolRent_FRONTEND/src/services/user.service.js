import httpClient from "../http-common";

const getAllClients = () => {
    return httpClient.get('/api/users/getUsers');
}

const createUser = (data) => {
  return httpClient.post(`/api/users/createUser`, data);
};

const get = (id) => {
  return httpClient.get(`api/users/${id}`);
}

export default { getAllClients, createUser, get };