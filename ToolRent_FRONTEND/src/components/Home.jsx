import { useKeycloak } from "@react-keycloak/web";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import Button from "@mui/material/Button";

const Home = () => {
  const { keycloak } = useKeycloak();

  const username = keycloak.tokenParsed?.preferred_username;

  return (
    <Box
      sx={{
        backgroundColor: "white",
        minHeight: "100vh",
        display: "flex",
        flexDirection: "column",
      }}
    >
      <Box sx={{ flexGrow: 1 }}>
        <Container maxWidth="lg" sx={{ mt: 8, mb: 8 }}>
          <Box
            sx={{
              textAlign: "center",
              py: 6,
              background: "linear-gradient(135deg, #e8f5e8 0%, #f1f8e9 100%)",
              borderRadius: 4,
              mb: 6,
            }}
          >
            {keycloak.authenticated ? (
              <>
                <Typography
                  variant="h3"
                  sx={{ fontWeight: "bold", color: "#1b5e20", mb: 2 }}
                >
                  👋 Hola, {username}
                </Typography>

                {/* Botón solo si está logueado */}
                <Button
                  variant="contained"
                  size="large"
                  sx={{
                    backgroundColor: "#1b5e20",
                    fontSize: "1.1rem",
                    py: 1.5,
                    px: 4,
                    borderRadius: 3,
                    "&:hover": { backgroundColor: "#2e7d32" },
                  }}
                  onClick={() => {
                    // Aquí puedes redirigir a la página de registrar préstamos
                    // Por ejemplo, si usas react-router-dom:
                    window.location.href = "/prestamos";
                  }}
                >
                  Registrar Préstamos
                </Button>
              </>
            ) : (
              <>
                <Typography
                  variant="h3"
                  sx={{ fontWeight: "bold", color: "#1b5e20", mb: 2 }}
                >
                  Toolrent: Sistema de Gestión de Préstamos de Herramientas
                </Typography>
                <Typography
                  variant="h6"
                  sx={{ color: "#2e7d32", mb: 4 }}
                >
                  Herramientas de construcción y reparación
                </Typography>
                <Button
                  variant="contained"
                  size="large"
                  sx={{
                    backgroundColor: "#1b5e20",
                    fontSize: "1.2rem",
                    py: 2,
                    px: 4,
                    borderRadius: 3,
                    "&:hover": { backgroundColor: "#2e7d32" },
                  }}
                  onClick={() => keycloak.login()}
                >
                  Iniciar sesión
                </Button>
              </>
            )}
          </Box>
        </Container>
      </Box>
    </Box>
  );
};

export default Home;

