import { useTheme } from '../hooks/useTheme';

export default function ThemeToggle() {
    const { theme, toggleTheme } = useTheme();

    return (
        <button
            onClick={toggleTheme}
            style={{
                background: 'none',
                border: 'none',
                fontSize: '1.5rem',
                cursor: 'pointer',
            }}
            aria-label="Переключить тему"
        >
            {theme === 'light' ? '🌙' : '☀️'}
        </button>
    );
}