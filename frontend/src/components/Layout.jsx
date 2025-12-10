import { Outlet } from 'react-router-dom';
import ThemeToggle from './ThemeToggle';
import '../styles/theme.css';
import '../styles/global.css';

export default function Layout() {
    return (
        <>
            <header className="header container">
                <h1>
                    <a href="/">Статьи</a>
                </h1>
                <ThemeToggle />
            </header>
            <main className="container">
                <Outlet />
            </main>
        </>
    );
}