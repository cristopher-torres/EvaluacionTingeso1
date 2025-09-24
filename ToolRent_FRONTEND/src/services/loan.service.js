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

export const getActiveLoansByDate = (startDate, endDate) => {
  return httpClient.get(`/api/loans/loansActiveByDate?startDate=${startDate}&endDate=${endDate}`);
};

export const getOverdueClients = () => {
  return httpClient.get("/api/loans/overdueClients");
};


export const getOverdueClientsByDate = (startDate, endDate) => {
  return httpClient.get(`/api/loans/overdueClients/dateRange?startDate=${startDate}&endDate=${endDate}`);
};

export default { returnLoan , createLoan, getActiveLoans, getLoans, updateFinePaid, getActiveLoansByDate, getOverdueClients, getOverdueClientsByDate };