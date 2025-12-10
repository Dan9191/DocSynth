import { useState, useEffect } from 'react';
import ArticleCard from '../components/ArticleCard';
import { getConfig } from '../config.js';

export default function Home() {
    const [articles, setArticles] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const { GET_ALL_SHORT_ARTICLES_URL } = getConfig();
        if (!GET_ALL_SHORT_ARTICLES_URL) return;

        fetch(GET_ALL_SHORT_ARTICLES_URL)
            .then(res => res.json())
            .then(data => {
                setArticles(data);
                setLoading(false);
            })
            .catch(() => setLoading(false));
    }, []);

    if (loading) {
        return (
            <div style={{ textAlign: 'center', padding: '4rem 0' }}>
                <p>Загрузка статей...</p>
            </div>
        );
    }

    return (
        <div className="article-grid">
            {articles.map(article => (
                <ArticleCard key={article.id} article={article} />
            ))}
        </div>
    );
}