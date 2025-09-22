import httpClient from "../http-common";

const createLoan = (data, quantity) => {
  return httpClient.post(`/api/loans/createLoan`, data);
};

export const returnLoan = (loanId, damaged = false, irreparable = false) => {
  return httpClient.post(`/api/loans/${loanId}/return?damaged=${damaged}&irreparable=${irreparable}`);
};


export const getLoans = () => {
  return httpClient.get("/api/loans/getLoans");
};


export const getActiveLoans = () => {
  return httpClient.get("/api/loans/loansActive");
};

export const updateFinePaid = (loanId, finePaid) => {
    return httpClient.put(`/api/loans/${loanId}/finePaid?finePaid=${finePaid}`);
};

export default { returnLoan , createLoan, getActiveLoans, getLoans, updateFinePaid };