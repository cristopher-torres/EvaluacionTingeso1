import { useEffect, useState } from "react";
import { getActiveLoans } from "../services/loan.service";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import Button from "@mui/material/Button";
import { returnLoan } from "../services/loan.service";

const ActiveLoanList = () => {
  const [loans, setLoans] = useState([]);

  const fetchLoans = () => {
    getActiveLoans().then(res => setLoans(res.data));
  };

  useEffect(() => {
    fetchLoans();
  }, []);

  const handleReturn = (id) => {
    returnLoan(id).then(() => fetchLoans());
  };

  return (
    <div>
      <h2>Préstamos Activos</h2>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>Herramienta</TableCell>
              <TableCell>Cliente</TableCell>
              <TableCell>Inicio</TableCell>
              <TableCell>Fecha límite</TableCell>
              <TableCell>Acción</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {loans.map(loan => (
              <TableRow key={loan.id}>
                <TableCell>{loan.id}</TableCell>
                <TableCell>{loan.tool?.id}</TableCell>
                <TableCell>{loan.client?.id}</TableCell>
                <TableCell>{new Date(loan.startDate).toLocaleDateString()}</TableCell>
                <TableCell>{new Date(loan.scheduledReturnDate).toLocaleDateString()}</TableCell>
                <TableCell>
                  <Button variant="contained" color="success" onClick={()=>handleReturn(loan.id)}>Devolver</Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
};

export default ActiveLoanList;
