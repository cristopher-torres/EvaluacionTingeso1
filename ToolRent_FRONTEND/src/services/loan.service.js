import httpClient from "../http-common";

const createLoan = (data, quantity) => {
  return httpClient.post(`/api/loans/createLoan`, data);
};

export const returnLoan = (loanId) => {
  return httpClient.post(`/api/loans/${loanId}/return`);
};

export const getLoans = () => {
  return httpClient.get("/api/loans/getLoans");
};


export const getActiveLoans = () => {
  return httpClient.get("/api/loans/loansActive");
};

export default { returnLoan , createLoan, getActiveLoans, getLoans };