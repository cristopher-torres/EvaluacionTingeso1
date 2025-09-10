import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import Button from "@mui/material/Button";
import Grid from "@mui/material/Grid";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";

const Home = () => {
  return (
    <Box sx={{
      backgroundColor: "white",
      minHeight: "100vh",
      display: "flex",
      flexDirection: "column"
    }}>
      <Box sx={{ flexGrow: 1 }}>
        {/* Hero Section */}
        <Container maxWidth="lg" sx={{ mt: 8, mb: 8 }}>
          <Box
            sx={{
              textAlign: 'center',
              py: 8,
              background: 'linear-gradient(135deg, #e8f5e8 0%, #f1f8e9 100%)',
              borderRadius: 4,
              mb: 6
            }}
          >
            <Typography
              variant="h2"
              component="h1"
              sx={{
                fontWeight: 'bold',
                color: '#1b5e20',
                mb: 3,
                fontSize: { xs: '2rem', md: '3.5rem' }
              }}
            >
              Arrienda las mejores herramientas para tu proyecto
            </Typography>
            <Typography
              variant="h5"
              sx={{
                color: '#2e7d32',
                mb: 4,
                fontSize: { xs: '1.2rem', md: '1.5rem' }
              }}
            >
              Herramientas de construcción y reparación al alcance de tu mano
            </Typography>
            <Box sx={{ display: 'flex', gap: 3, justifyContent: 'center', flexWrap: 'wrap' }}>
              <Button
                variant="contained"
                size="large"
                sx={{
                  backgroundColor: '#1b5e20',
                  fontSize: '1.2rem',
                  py: 2,
                  px: 4,
                  borderRadius: 3,
                  '&:hover': {
                    backgroundColor: '#2e7d32',
                  }
                }}
              >
                Registrarse Ahora
              </Button>
            </Box>
          </Box>

          {/* Beneficios Clave */}
          <Typography
            variant="h3"
            component="h2"
            sx={{
              textAlign: 'center',
              color: '#1b5e20',
              mb: 5,
              fontWeight: 'bold'
            }}
          >
            ¿Por qué elegir ToolRent?
          </Typography>

          <Grid container spacing={3} sx={{ mb: 6, justifyContent: 'center' }}>
            <Grid item xs={12} sm={6} md={3}>
              <Card 
                sx={{ 
                  height: '100%', 
                  textAlign: 'center',
                  transition: 'transform 0.3s, box-shadow 0.3s',
                  maxWidth: 280,
                  mx: 'auto',
                  '&:hover': {
                    transform: 'translateY(-4px)',
                    boxShadow: '0 8px 16px rgba(0,0,0,0.12)'
                  }
                }}
              >
                <CardContent sx={{ py: 3, px: 2 }}>
                  <Typography 
                    variant="h3" 
                    sx={{ color: '#1b5e20', mb: 1.5, fontSize: '2rem' }}
                  >
                    🔧
                  </Typography>
                  <Typography variant="h6" sx={{ fontWeight: 'bold', color: '#1b5e20', mb: 1.5, fontSize: '1.1rem' }}>
                    Amplio Catálogo
                  </Typography>
                  <Typography variant="body2" sx={{ color: '#666', fontSize: '0.9rem' }}>
                    Miles de herramientas profesionales para construcción y reparación.
                  </Typography>
                </CardContent>
              </Card>
            </Grid>

            <Grid item xs={12} sm={6} md={3}>
              <Card 
                sx={{ 
                  height: '100%', 
                  textAlign: 'center',
                  transition: 'transform 0.3s, box-shadow 0.3s',
                  maxWidth: 280,
                  mx: 'auto',
                  '&:hover': {
                    transform: 'translateY(-4px)',
                    boxShadow: '0 8px 16px rgba(0,0,0,0.12)'
                  }
                }}
              >
                <CardContent sx={{ py: 3, px: 2 }}>
                  <Typography 
                    variant="h3" 
                    sx={{ color: '#1b5e20', mb: 1.5, fontSize: '2rem' }}
                  >
                    ⏱️
                  </Typography>
                  <Typography variant="h6" sx={{ fontWeight: 'bold', color: '#1b5e20', mb: 1.5, fontSize: '1.1rem' }}>
                    Proceso Rápido
                  </Typography>
                  <Typography variant="body2" sx={{ color: '#666', fontSize: '0.9rem' }}>
                    Te brindamos una experiencia rapida en el proceso de arrendar una herramienta.
                  </Typography>
                </CardContent>
              </Card>
            </Grid>

            <Grid item xs={12} sm={6} md={3}>
              <Card 
                sx={{ 
                  height: '100%', 
                  textAlign: 'center',
                  transition: 'transform 0.3s, box-shadow 0.3s',
                  maxWidth: 280,
                  mx: 'auto',
                  '&:hover': {
                    transform: 'translateY(-4px)',
                    boxShadow: '0 8px 16px rgba(0,0,0,0.12)'
                  }
                }}
              >
                <CardContent sx={{ py: 3, px: 2 }}>
                  <Typography 
                    variant="h3" 
                    sx={{ color: '#1b5e20', mb: 1.5, fontSize: '2rem' }}
                  >
                    💰
                  </Typography>
                  <Typography variant="h6" sx={{ fontWeight: 'bold', color: '#1b5e20', mb: 1.5, fontSize: '1.1rem' }}>
                    Tarifas Competitivas
                  </Typography>
                  <Typography variant="body2" sx={{ color: '#666', fontSize: '0.9rem' }}>
                    Precios justos por día de arriendo.
                  </Typography>
                </CardContent>
              </Card>
            </Grid>

            <Grid item xs={12} sm={6} md={3}>
              <Card 
                sx={{ 
                  height: '100%', 
                  textAlign: 'center',
                  transition: 'transform 0.3s, box-shadow 0.3s',
                  maxWidth: 280,
                  mx: 'auto',
                  '&:hover': {
                    transform: 'translateY(-4px)',
                    boxShadow: '0 8px 16px rgba(0,0,0,0.12)'
                  }
                }}
              >
                <CardContent sx={{ py: 3, px: 2 }}>
                  <Typography 
                    variant="h3" 
                    sx={{ color: '#1b5e20', mb: 1.5, fontSize: '2rem' }}
                  >
                    🛡️
                  </Typography>
                  <Typography variant="h6" sx={{ fontWeight: 'bold', color: '#1b5e20', mb: 1.5, fontSize: '1.1rem' }}>
                    Herramientas de Calidad
                  </Typography>
                  <Typography variant="body2" sx={{ color: '#666', fontSize: '0.9rem' }}>
                    Equipos profesionales de la mejor calidad.
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          </Grid>
        </Container>
      </Box>

      {/* Footer con información adicional */}
      <Box
        sx={{
          background: "linear-gradient(135deg, #2e7d32 0%, #1b5e20 100%)",
          color: "white",
          py: 6,
        }}
      >
        <Container maxWidth="lg">
          <Grid container spacing={4}>
            {/* Contacto */}
            <Grid item xs={12} md={4}>
              <Typography variant="h6" sx={{ mb: 2, fontWeight: 'bold' }}>
                📞 Contacto
              </Typography>
              <Typography variant="body2" sx={{ mb: 1, opacity: 0.9 }}>
                Teléfono: +56 2 2345 6789
              </Typography>
              <Typography variant="body2" sx={{ mb: 1, opacity: 0.9 }}>
                Email: info@toolrent.cl
              </Typography>
              <Typography variant="body2" sx={{ opacity: 0.9 }}>
                WhatsApp: +56 9 8765 4321
              </Typography>
            </Grid>

            {/* Sucursales */}
            <Grid item xs={12} md={4}>
              <Typography variant="h6" sx={{ mb: 2, fontWeight: 'bold' }}>
                📍 Sucursales
              </Typography>
              <Typography variant="body2" sx={{ mb: 1, opacity: 0.9 }}>
                <strong>Santiago Centro:</strong> Av. Libertador 1234
              </Typography>
              <Typography variant="body2" sx={{ mb: 1, opacity: 0.9 }}>
                <strong>Las Condes:</strong> Av. Apoquindo 5678
              </Typography>
              <Typography variant="body2" sx={{ opacity: 0.9 }}>
                <strong>Maipú:</strong> Av. Pajaritos 9012
              </Typography>
            </Grid>

            {/* Horarios */}
            <Grid item xs={12} md={4}>
              <Typography variant="h6" sx={{ mb: 2, fontWeight: 'bold' }}>
                🕒 Horarios de Atención
              </Typography>
              <Typography variant="body2" sx={{ mb: 1, opacity: 0.9 }}>
                <strong>Lunes a Viernes:</strong> 8:00 - 18:00
              </Typography>
              <Typography variant="body2" sx={{ mb: 1, opacity: 0.9 }}>
                <strong>Sábados:</strong> 9:00 - 15:00
              </Typography>
              <Typography variant="body2" sx={{ opacity: 0.9 }}>
                <strong>Domingos:</strong> Cerrado
              </Typography>
            </Grid>
          </Grid>

          {/* Línea separadora y copyright */}
          <Box sx={{ borderTop: '1px solid rgba(255,255,255,0.2)', mt: 4, pt: 3, textAlign: 'center' }}>
            <Typography variant="h6" sx={{ mb: 1, fontWeight: 'bold' }}>
              ToolRent - Sistema de Gestión de Herramientas
            </Typography>
            <Typography variant="body2" sx={{ opacity: 0.8 }}>
              © 2025 ToolRent. Todos los derechos reservados.
            </Typography>
          </Box>
        </Container>
      </Box>
    </Box>
  );
};

export default Home;