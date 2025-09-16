import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import { useKeycloak } from "@react-keycloak/web";
import { useNavigate } from 'react-router-dom';
import logo from "../assets/ToolRent_Logo.png";
import Sidemenu from "./Sidemenu";
import { useState } from 'react';

function DesktopAppBar() {
  const { keycloak } = useKeycloak();
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);

  const toggleDrawer = (newOpen) => (event) => {
    setOpen(newOpen);
  };

  return (
    <>
      <AppBar position="static" sx={{ backgroundColor: '#1b5e20', width: "100%" }}>
        <Container maxWidth={false} disableGutters sx={{ px: 0 }}>
          <Toolbar disableGutters sx={{ px: 0, pl: 2, pr: 2 }}>
            {/* Botón menú hamburguesa */}
            <IconButton
              size="large"
              edge="start"
              color="inherit"
              aria-label="menu"
              sx={{ mr: 2 }}
              onClick={toggleDrawer(true)} // 👈 abre el sidemenu
            >
              <MenuIcon />
            </IconButton>

            {/* Logo + nombre */}
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
                textDecoration: 'none'
              }}
            >
              <img
                src={logo}
                alt="ToolRent Logo"
                style={{ height: 60, marginRight: 5 }}
              />
              ToolRent
            </Typography>

            {/* Botones de navegación */}
            <div style={{ flexGrow: 1, display: 'flex', justifyContent: 'flex-end', marginRight: '60px' }}>
              {/* Inventario */}
              <Button
                onClick={() => {
                  if (!keycloak.authenticated) {
                    alert("Debes iniciar sesión para acceder al inventario.");
                    return;
                  }

                  // Verifica roles
                  const hasRole = keycloak.hasRealmRole("ADMIN") || keycloak.hasRealmRole("EMPLOYEE");
                  if (!hasRole) {
                    alert("No tienes permisos para acceder al inventario.");
                    return;
                  }

                  // Si pasa todas las validaciones, navega
                  navigate("/inventario");
                }}
                sx={{
                  my: 2,
                  color: 'white',
                  backgroundColor: 'rgba(255, 255, 255, 0.1)',
                  marginLeft: 2,
                  borderRadius: 1,
                  textTransform: 'none',
                  '&:hover': {
                    backgroundColor: '#ffeb3b',
                    color: 'black',
                  }
                }}
              >
                Inventario
              </Button>

              {/* Login / Logout dinámico */}
              {keycloak.authenticated ? (
                <Button
                  onClick={() => keycloak.logout()}
                  sx={{
                    my: 2,
                    color: 'white',
                    backgroundColor: 'rgba(255, 255, 255, 0.1)',
                    marginLeft: 2,
                    borderRadius: 1,
                    textTransform: 'none',
                    '&:hover': {
                      backgroundColor: '#ffeb3b',
                      color: 'black',
                    }
                  }}
                >
                  Cerrar sesión
                </Button>
              ) : (
                <Button
                  onClick={() => keycloak.login()}
                  sx={{
                    my: 2,
                    color: 'white',
                    backgroundColor: 'rgba(255, 255, 255, 0.1)',
                    marginLeft: 2,
                    borderRadius: 1,
                    textTransform: 'none',
                    '&:hover': {
                      backgroundColor: '#ffeb3b',
                      color: 'black',
                    }
                  }}
                >
                  Iniciar sesión
                </Button>
              )}
            </div>
          </Toolbar>
        </Container>
      </AppBar>

      {/* Side menu */}
      <Sidemenu open={open} toggleDrawer={toggleDrawer} />
    </>
  );
}

export default DesktopAppBar;

