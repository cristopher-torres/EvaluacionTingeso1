import './App.css'
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom'
import Navbar from "./components/Navbar"
import Home from './components/Home';
import NotFound from './components/NotFound';
import { useKeycloak } from "@react-keycloak/web";
import ToolList from './components/ToolList';
import AddEditTool from './components/AddEditTool';

function App() {
  const { keycloak, initialized } = useKeycloak();

  if (!initialized) return <div>Cargando...</div>;

  const isLoggedIn = keycloak.authenticated;
  const roles = keycloak.tokenParsed?.realm_access?.roles || [];

  const PrivateRoute = ({ element, rolesAllowed }) => {
    if (!isLoggedIn) {
      keycloak.login();
      return null;
    }
    if (rolesAllowed && !rolesAllowed.some(r => roles.includes(r))) {
      return <h2>No tienes permiso para ver esta página</h2>;
    }
    return element;
  };
  
  return (
      <Router>
          <div className="container">
          <Navbar />
            <Routes>
              <Route path="/" element={<Home/>} />
              <Route path="/inventario" element={<PrivateRoute element={<ToolList />} rolesAllowed={['EMPLOYEE', 'ADMIN']} />} />
              <Route path="/tools/add" element={<PrivateRoute element={<AddEditTool />} rolesAllowed={['EMPLOYEE', 'ADMIN']} />} />
              <Route path="*" element={<NotFound/>} />
            </Routes>
          </div>
      </Router>
  );
}

export default App