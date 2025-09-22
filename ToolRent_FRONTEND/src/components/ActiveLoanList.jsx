import { useEffect, useState } from "react";
import { getActiveLoans, returnLoan, updateFinePaid } from "../services/loan.service";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogTitle from "@mui/material/DialogTitle";
import FormControlLabel from "@mui/material/FormControlLabel";
import Checkbox from "@mui/material/Checkbox";

const ActiveLoanList = () => {
  const [loans, setLoans] = useState([]);
  const [filter, setFilter] = useState("");

  const [openDialog, setOpenDialog] = useState(false);
  const [selectedLoan, setSelectedLoan] = useState(null);
  const [damaged, setDamaged] = useState(false);
  const [irreparable, setIrreparable] = useState(false);

  const [openReceiptDialog, setOpenReceiptDialog] = useState(false);
  const [loanReceipt, setLoanReceipt] = useState(null);

  const fetchLoans = () => {
    getActiveLoans().then(res => setLoans(res.data));
  };

  useEffect(() => {
    fetchLoans();
  }, []);

  const handleOpenDialog = (loan) => {
    setSelectedLoan(loan);
    setDamaged(false);
    setIrreparable(false);
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setSelectedLoan(null);
  };

  const handleReturn = () => {
    if (!selectedLoan) return;

    returnLoan(selectedLoan.id, damaged, irreparable, true).then(res => {
      setLoanReceipt(res.data);
      setOpenReceiptDialog(true);
      setOpenDialog(false);
    });
  };

  const handleFinePaid = (paid) => {
    if (!loanReceipt || !loanReceipt.id) return;

    updateFinePaid(loanReceipt.id, paid).then(updatedLoan => {
      setLoanReceipt(prev => ({
        ...prev,
        finePaid: paid
      }));
      
      
      setOpenReceiptDialog(false);
      setSelectedLoan(null);
      fetchLoans();
    }).catch(error => {
      console.error('Error actualizando estado de multa:', error);
    });
  };

  const formatDate = (dateString) => {
    if (!dateString) return "";
    const [year, month, day] = dateString.split("-");
    return `${day}/${month}/${year}`;
  };

  const filteredLoans = loans.filter(loan => {
    if (!filter) return true;
    const rut = loan.client?.rut || "";
    return rut.includes(filter) || loan.id.toString().includes(filter);
  });

  return (
    <div>
      <h2>Préstamos Activos</h2>

      <TextField
        label="Filtrar por RUT o ID del préstamo"
        variant="outlined"
        value={filter}
        onChange={(e) => setFilter(e.target.value)}
        style={{ marginBottom: "20px" }}
      />

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>Herramienta</TableCell>
              <TableCell>Cliente (ID)</TableCell>
              <TableCell>RUT (Cliente)</TableCell>
              <TableCell>Inicio</TableCell>
              <TableCell>Fecha límite</TableCell>
              <TableCell>Estado</TableCell>
              <TableCell>Acción</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredLoans.map(loan => (
              <TableRow key={loan.id}>
                <TableCell>{loan.id}</TableCell>
                <TableCell>{loan.tool?.name}</TableCell>
                <TableCell>{loan.client?.id}</TableCell>
                <TableCell>{loan.client?.rut}</TableCell>
                <TableCell>{formatDate(loan.startDate)}</TableCell>
                <TableCell>{formatDate(loan.scheduledReturnDate)}</TableCell>
                <TableCell>{loan.loanStatus}</TableCell>
                <TableCell>
                  <Button 
                    variant="contained" 
                    color="success" 
                    onClick={() => handleOpenDialog(loan)}
                  >
                    Devolver
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {/* Dialog para indicar daño */}
      <Dialog open={openDialog} onClose={handleCloseDialog}>
        <DialogTitle>Devolver Herramienta</DialogTitle>
        <DialogContent>
          <FormControlLabel
            control={
              <Checkbox
                checked={damaged}
                onChange={(e) => setDamaged(e.target.checked)}
              />
            }
            label="Herramienta dañada"
          />
          <FormControlLabel
            control={
              <Checkbox
                checked={irreparable}
                onChange={(e) => setIrreparable(e.target.checked)}
                disabled={!damaged}
              />
            }
            label="Daño irreparable"
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancelar</Button>
          <Button variant="contained" color="success" onClick={handleReturn}>
            Confirmar devolución
          </Button>
        </DialogActions>
      </Dialog>

      {/* Dialog de boleta/resumen con pregunta sobre multa */}
      <Dialog open={openReceiptDialog} onClose={() => setOpenReceiptDialog(false)}>
        <DialogTitle>Boleta de Devolución</DialogTitle>
        <DialogContent>
          {loanReceipt && (
            <div style={{ minWidth: "300px" }}>
              <p><strong>Cliente:</strong> {loanReceipt.client?.rut} (ID: {loanReceipt.client?.id})</p>
              <p><strong>Herramienta:</strong> {loanReceipt.tool?.name}</p>
              <p><strong>Precio préstamo:</strong> ${loanReceipt.loanPrice?.toFixed(2) || '0.00'}</p>
              <p><strong>Multa por atraso:</strong> ${loanReceipt.fine?.toFixed(2) || '0.00'}</p>
              <p><strong>Daño:</strong> ${loanReceipt.damagePrice?.toFixed(2) || '0.00'}</p>
              <p><strong>Total multa + daño:</strong> ${loanReceipt.fineTotal?.toFixed(2) || '0.00'}</p>
              <p><strong>Total a pagar:</strong> ${loanReceipt.total?.toFixed(2) || '0.00'}</p>

              {/* Solo mostrar botones si hay multa que pagar */}
              {loanReceipt.fineTotal > 0 && (
                <>
                  <p style={{ fontWeight: "bold", marginTop: "20px", marginBottom: "10px" }}>
                    ¿El cliente pagó la multa?
                  </p>
                  <div style={{ display: "flex", gap: "10px", justifyContent: "center" }}>
                    <Button 
                      variant="contained" 
                      color="success" 
                      onClick={() => handleFinePaid(true)}
                    >
                      Sí, pagó
                    </Button>
                    <Button 
                      variant="contained" 
                      color="error" 
                      onClick={() => handleFinePaid(false)}
                    >
                      No pagó
                    </Button>
                  </div>
                </>
              )}

              {/* Si no hay multa, solo mostrar botón cerrar */}
              {loanReceipt.fineTotal <= 0 && (
                <div style={{ display: "flex", justifyContent: "center", marginTop: "20px" }}>
                  <Button 
                    variant="contained" 
                    onClick={() => {
                      setOpenReceiptDialog(false);
                      setSelectedLoan(null);
                      fetchLoans();
                    }}
                  >
                    Cerrar
                  </Button>
                </div>
              )}
            </div>
          )}
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default ActiveLoanList;











