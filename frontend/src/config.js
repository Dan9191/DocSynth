let config = {
    GET_ALL_SHORT_ARTICLES_URL: '',
    GET_ARTICLE_BY_ID_URL: ''
};

export const loadConfig = async () => {
    try {
        const res = await fetch('/config.json', { cache: 'no-store' });
        if (res.ok) {
            const data = await res.json();
            config = { ...config, ...data };
            console.log('Config loaded:', config);
        } else {
            console.warn('config.json not found (404)');
        }
    } catch (err) {
        console.error('Failed to load config.json:', err);
    }
};

export const getConfig = () => config;