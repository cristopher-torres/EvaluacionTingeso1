import './App.css'
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom'
import Navbar from "./components/Navbar"
import Home from './components/Home';
import NotFound from './components/NotFound';
import { useKeycloak } from "@react-keycloak/web";
import ToolList from './components/ToolList';
import AddEditTool from './components/AddEditTool';
import LoanList from './components/LoanList';
import AddLoan from './components/AddLoan';
import ActiveLoanList from './components/ActiveLoanList';
import AddUser from './components/AddUser';
import ToolDecommission from './components/ToolDecommission';

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
              <Route path="/tools/edit/:id" element={<PrivateRoute element={<AddEditTool />} rolesAllowed={['ADMIN']} />} />
              <Route path="/prestamos" element={<PrivateRoute element={<AddLoan />} rolesAllowed={['EMPLOYEE', 'ADMIN']} />} />
              <Route path="/loans/ActiveLoanList" element={<PrivateRoute element={<ActiveLoanList />} rolesAllowed={['EMPLOYEE', 'ADMIN']} />} />
              <Route path="/loans/list" element={<PrivateRoute element={<LoanList />} rolesAllowed={['EMPLOYEE', 'ADMIN']} />} />
              <Route path="/user/userRegistrer" element={<PrivateRoute element={<AddUser />} rolesAllowed={['ADMIN']} />} />
              <Route path="/tools/decommission" element={<PrivateRoute element={<ToolDecommission />} rolesAllowed={['ADMIN']} />} />
              <Route path="*" element={<NotFound/>} />
            </Routes>
          </div>
      </Router>
  );
}

export default App