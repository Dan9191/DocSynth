import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import ArticleContent from '../components/ArticleContent';
import { getConfig } from '../config.js';

export default function ArticlePage() {
    const { id } = useParams();
    const [article, setArticle] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const { GET_ARTICLE_BY_ID_URL } = getConfig();
        if (!GET_ARTICLE_BY_ID_URL) return;

        fetch(`${GET_ARTICLE_BY_ID_URL}${id}`)
            .then(res => res.json())
            .then(data => {
                setArticle(data);
                setLoading(false);
            })
            .catch(() => setLoading(false));
    }, [id]);
    if (loading) {
        return (
            <div className="article-page">
                <p style={{ textAlign: 'center', padding: '4rem 0' }}>Загрузка статьи...</p>
            </div>
        );
    }

    if (!article) {
        return (
            <div className="article-page">
                <p style={{ textAlign: 'center', padding: '4rem 0' }}>Статья не найдена</p>
            </div>
        );
    }

    return (
        <article className="article-page">
            {article.mainPicture && (
                <img src={article.mainPicture} alt={article.title} />
            )}
            <h1>{article.title}</h1>
            <p>{article.description}</p>

            <div className="tags" style={{ margin: '1.5rem 0' }}>
                {article.tags.map(tag => (
                    <span key={tag.id} className="tag">{tag.name}</span>
                ))}
            </div>

            {article.source && (
                <p style={{ margin: '1.5rem 0' }}>
                    Источник: <p>{article.source}</p>
                </p>
            )}

            <ArticleContent body={article.body} />

            <Link
                to="/"
                style={{
                    display: 'inline-block',
                    marginTop: '2rem',
                    padding: '0.75rem 1.5rem',
                    background: 'var(--card-bg)',
                    color: 'var(--text-color)',
                    textDecoration: 'none',
                    borderRadius: '8px',
                    border: '1px solid var(--border-color)'
                }}
            >
                ← На главную
            </Link>
        </article>
    );
}