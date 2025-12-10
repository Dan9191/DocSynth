import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import Home from './pages/Home';
import ArticlePage from './pages/ArticlePage';

export default function App() {
    return (
        <Router basename="/">
            <Routes>
                <Route element={<Layout />}>
                    <Route path="/" element={<Home />} />
                    <Route path="/article/:id" element={<ArticlePage />} />
                </Route>
            </Routes>
        </Router>
    );
}