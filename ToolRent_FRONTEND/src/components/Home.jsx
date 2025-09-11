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
      {/* Contenido principal */}
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
                  ToolRent: Sistema de Gestión de Herramientas
                </Typography>
                <Typography
                  variant="h6"
                  sx={{ color: "#2e7d32", mb: 4 }}
                >
                  Herramientas de construcción y reparación.
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

      {/* Footer */}
      <Box
        sx={{
          borderTop: "1px solid rgba(255,255,255,0.2)",
          mt: 4,
          pt: 3,
          pb: 3,
          textAlign: "center",
          backgroundColor: "#1b5e20",
          color: "white",
        }}
      >
        <Typography variant="h6" sx={{ mb: 1, fontWeight: "bold" }}>
          ToolRent - Sistema de Gestión de Herramientas
        </Typography>
        <Typography variant="body2" sx={{ opacity: 0.8 }}>
          © 2025 ToolRent. Todos los derechos reservados.
        </Typography>
      </Box>
    </Box>
  );
};

export default Home;




