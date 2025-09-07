import axios from "axios";

const toolRentBackendServer = import.meta.env.VITE_TOOLRENT_BACKEND_SERVER;
const toolRentBackendPort = import.meta.env.VITE_TOOLRENT_BACKEND_PORT;

console.log(toolRentBackendServer)
console.log(toolRentBackendPort)

export default axios.create({
    baseURL: `http://${toolRentBackendServer}:${toolRentBackendPort}`,
    headers: {
        'Content-Type': 'application/json'
    }
});