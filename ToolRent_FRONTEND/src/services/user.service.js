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

const updateUserStatus = (userId, finePaid) => {
    return httpClient.put(`/api/users/${userId}/status?finePaid=${finePaid}`);
}

export default { getAllClients, createUser, get, updateUserStatus };