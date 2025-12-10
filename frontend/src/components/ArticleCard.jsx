import { Link } from 'react-router-dom';

export default function ArticleCard({ article }) {
    return (
        <Link to={`/article/${article.id}`} className="article-card">
            {article.mainPicture && (
                <img src={article.mainPicture} alt={article.title} />
            )}
            <div className="article-card-content">
                <h3>{article.title}</h3>
                <p>{article.description}</p>
            </div>
        </Link>
    );
}