import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import Button from '@mui/material/Button';

// Importa tu logo
import logo from "../assets/ToolRent_Logo.png";

const pages = ['Catalogo', 'Registrarse', 'Iniciar sesión'];

function DesktopAppBar() {
  const [anchorElUser, setAnchorElUser] = React.useState(null);

  const handleOpenUserMenu = (event) => {
    setAnchorElUser(event.currentTarget);
  };

  const handleCloseUserMenu = () => {
    setAnchorElUser(null);
  };

  return (
    <AppBar position="static" sx={{ backgroundColor: '#1b5e20', width: "100%" }}>
      <Container maxWidth={false} disableGutters sx={{ px: 0 }}>
        <Toolbar disableGutters sx={{ px: 0 }}>
          {/* Logo */}
          <Typography
            variant="h6"
            noWrap
            component="a"
            href="/"
            sx={{
              mr: 4,
              display: 'flex',
              alignItems: 'center',
              color: 'inherit',
              textDecoration: 'none',
              pl: 2
            }}
          >
            <img
              src={logo}
              alt="ToolRent Logo"
              style={{ height: 80, marginRight: 5 }}
            />
            ToolRent
          </Typography>

          {/* Botones de navegación */}
          <div style={{ flexGrow: 1, display: 'flex', justifyContent: 'flex-end', marginRight: '60px' }}>
            {pages.map((page) => (
              <Button
                key={page}
                sx={{
                  my: 2,
                  color: 'white',
                  backgroundColor: 'rgba(255, 255, 255, 0.1)',
                  display: 'inline-block',
                  marginLeft: 2, 
                  borderRadius: 1,
                  textTransform: 'none',
                  '&:hover': {
                    backgroundColor: '#ffeb3b',
                    color: 'black',
                  }
                }}
              >
                {page}
              </Button>
            ))}
          </div>
        </Toolbar>
      </Container>
    </AppBar>
  );
}

export default DesktopAppBar;

