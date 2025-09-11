import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import toolService from "../services/tool.service";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import AddIcon from "@mui/icons-material/Add";
import EditIcon from "@mui/icons-material/Edit";

const ToolList = () => {
  const [tools, setTools] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");

  const init = () => {
    toolService
      .getAll()
      .then((response) => {
        console.log("Mostrando listado de todas las herramientas.", response.data);
        setTools(response.data);
      })
      .catch((error) => {
        console.log(
          "Se ha producido un error al intentar mostrar listado de todas las herramientas.",
          error
        );
      });
  };

  useEffect(() => {
    init();
  }, []);

  // Filtrar herramientas según búsqueda
  const filteredTools = tools.filter((tool) => {
    const term = searchTerm.toLowerCase();
    return (
      tool.id.toString().includes(term) ||
      tool.name.toLowerCase().includes(term) ||
      tool.category.toLowerCase().includes(term)
    );
  });

  return (
    <div>
      <h2>Listado de Herramientas</h2>

      <TextField
        label="Buscar por ID, Nombre o Categoría"
        variant="outlined"
        size="small"
        fullWidth
        sx={{ mb: 2 }}
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
      />

      <Button
        variant="contained"
        startIcon={<AddIcon />}
        component={Link}
        to="/tools/add"
        sx={{
          mb: 2,
          backgroundColor: "#1b5e20",
          "&:hover": {
            backgroundColor: "#2e7d32"
          },
        }}
      >
        Agregar herramienta
      </Button>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>Nombre</TableCell>
              <TableCell>Categoría</TableCell>
              <TableCell>Estado</TableCell>
              <TableCell>Acciones</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredTools.map((tool) => (
              <TableRow key={tool.id}>
                <TableCell>{tool.id}</TableCell>
                <TableCell>{tool.name}</TableCell>
                <TableCell>{tool.category}</TableCell>
                <TableCell>{tool.status}</TableCell>
                <TableCell>
                  <Button
                    variant="outlined"
                    color="primary"
                    startIcon={<EditIcon />}
                    component={Link}
                    to={`/tools/edit/${tool.id}`}
                    sx={{ mr: 1 }}
                  >
                    Editar
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
};

export default ToolList;



