import { useState } from 'react';

export default function ArticleContent({ body }) {
    const [copiedIndex, setCopiedIndex] = useState(null);

    const copyToClipboard = async (text, index) => {
        await navigator.clipboard.writeText(text);
        setCopiedIndex(index);
        setTimeout(() => setCopiedIndex(null), 2000);
    };

    return (
        <div>
            {body.map((block, i) => {
                switch (block.type) {
                    case 'heading':
                        return <h2 key={i} style={{ margin: '1.5rem 0 1rem' }}>{block.content}</h2>;

                    case 'paragraph':
                        return <p key={i} style={{ lineHeight: 1.7 }}>{block.content}</p>;

                    case 'list':
                        return (
                            <ul key={i} style={{ paddingLeft: '1.2rem', lineHeight: 1.7 }}>
                                {block.items.map((item, idx) => (
                                    <li key={idx}>{item}</li>
                                ))}
                            </ul>
                        );

                    case 'text':
                        return <p key={i} style={{ lineHeight: 1.7 }}>{block.content}</p>;

                    case 'code':
                        return (
                            <div key={i} className="code-block">
                                <pre style={{ margin: 0, whiteSpace: 'pre-wrap', wordBreak: 'break-word' }}>
                                    <code>{block.content}</code>
                                </pre>
                                <button
                                    className="copy-btn"
                                    onClick={() => copyToClipboard(block.content, i)}
                                >
                                    {copiedIndex === i ? 'Скопировано!' : 'Копировать'}
                                </button>
                            </div>
                        );

                    default:
                        return null;
                }
            })}
        </div>
    );
}
