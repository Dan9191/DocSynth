import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import App from './App.jsx';
import { loadConfig } from './config.js';

const root = createRoot(document.getElementById('root'));

loadConfig().then(() => {
    root.render(
        <StrictMode>
            <App />
        </StrictMode>
    );
});