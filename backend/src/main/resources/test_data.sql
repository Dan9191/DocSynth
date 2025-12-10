-- ==========================================
-- Тестовые данные для таблиц тегов, секций и статей
-- ==========================================

-- 1. Теги

set search_path to article;
INSERT INTO tag (name) VALUES
                           ('Kubernetes'),
                           ('DevOps'),
                           ('Linux'),
                           ('PostgreSQL'),
                           ('Frontend'),
                           ('JSON')
    ON CONFLICT (name) DO NOTHING;

-- 2. Секции (например, "Инфраструктура" и "Базы данных")
INSERT INTO article_section (name, description, parent_id) VALUES
                                                               ('Инфраструктура', 'Материалы о системах, кластеризации и DevOps', NULL),
                                                               ('Базы данных', 'Статьи о СУБД и оптимизации запросов', NULL),
                                                               ('Web-разработка', 'Советы и практики фронтенда', NULL)
    ON CONFLICT DO NOTHING;

-- 3. Статьи
-- Пример структуры JSON body: массив блоков с типом и содержимым
INSERT INTO article (title, description, main_picture, source, body, section_id)
VALUES
    (
        'Настройка кластера Kubernetes на двух VPS',
        'Пошаговое руководство по настройке Kubernetes кластера с Flannel и systemd',
        'https://example.com/images/k8s-cluster.jpg',
        'https://example.com/setup-k8s',
        '[
            {"type": "heading", "content": "Введение"},
            {"type": "text", "content": "В этом руководстве мы создадим кластер Kubernetes из двух VPS."},
            {"type": "code", "content": "sudo modprobe br_netfilter\\nsysctl -w net.bridge.bridge-nf-call-iptables=1"},
            {"type": "image", "src": "https://example.com/images/flannel-diagram.png"},
            {"type": "text", "content": "Теперь кластер готов к запуску Flannel и развёртыванию приложений."}
        ]'::jsonb,
        (SELECT id FROM article_section WHERE name = 'Инфраструктура')
    ),
    (
        'Оптимизация запросов в PostgreSQL',
        'Как ускорить сложные SELECT-запросы и анализировать планы выполнения',
        'https://example.com/images/postgres-optimization.jpg',
        'https://example.com/pg-optimization',
        '[
            {"type": "heading", "content": "EXPLAIN ANALYZE"},
            {"type": "text", "content": "Команда EXPLAIN ANALYZE показывает, как реально выполняется запрос."},
            {"type": "code", "content": "EXPLAIN ANALYZE SELECT * FROM users WHERE age > 30;"},
            {"type": "text", "content": "Используйте индексы для ускорения фильтрации по полям."}
        ]'::jsonb,
        (SELECT id FROM article_section WHERE name = 'Базы данных')
    ),
    (
        'Работа с JSON на фронтенде',
        'Парсинг, валидация и рендеринг JSON-данных в браузере',
        'https://example.com/images/json-frontend.jpg',
        'https://example.com/json-front',
        '[
            {"type": "heading", "content": "JSON и UI"},
            {"type": "text", "content": "Каждый блок статьи можно отрисовать динамически по структуре JSON."},
            {"type": "code", "content": "const blocks = JSON.parse(article.body);"},
            {"type": "text", "content": "Это позволяет гибко управлять контентом на клиенте."}
        ]'::jsonb,
        (SELECT id FROM article_section WHERE name = 'Web-разработка')
    );

-- 4. Привязка тегов к статьям
INSERT INTO article_tag (article_id, tag_id)
SELECT a.id, t.id FROM article a
                           JOIN tag t ON
    (a.title = 'Настройка кластера Kubernetes на двух VPS' AND t.name IN ('Kubernetes', 'Linux', 'DevOps'))
        OR (a.title = 'Оптимизация запросов в PostgreSQL' AND t.name IN ('PostgreSQL', 'Linux'))
        OR (a.title = 'Работа с JSON на фронтенде' AND t.name IN ('Frontend', 'JSON'));

-- 5. Привязка тегов к секциям
INSERT INTO article_section_tag (section_id, tag_id)
SELECT s.id, t.id FROM article_section s
                           JOIN tag t ON
    (s.name = 'Инфраструктура' AND t.name IN ('Kubernetes', 'DevOps'))
        OR (s.name = 'Базы данных' AND t.name IN ('PostgreSQL'))
        OR (s.name = 'Web-разработка' AND t.name IN ('Frontend', 'JSON'));

-- ==========================================
-- Проверка
-- ==========================================
-- SELECT * FROM tag;
-- SELECT * FROM article_section;
-- SELECT title, section_id FROM article;
-- SELECT * FROM article_tag;
-- SELECT * FROM article_section_tag;



set search_path to article;
INSERT INTO tag (name) VALUES
                           ('Kubernetes'),
                           ('DevOps'),
                           ('Linux'),
                           ('PostgreSQL'),
                           ('Frontend'),
                           ('JSON')
ON CONFLICT (name) DO NOTHING;

-- 2. Секции (например, "Инфраструктура" и "Базы данных")
INSERT INTO article_section (name, description, parent_id) VALUES
                                                               ('Инфраструктура', 'Материалы о системах, кластеризации и DevOps', NULL),
                                                               ('Базы данных', 'Статьи о СУБД и оптимизации запросов', NULL),
                                                               ('Web-разработка', 'Советы и практики фронтенда', NULL)
ON CONFLICT DO NOTHING;

INSERT INTO article (
    title,
    description,
    main_picture,
    source,
    body,
    section_id
)
SELECT
    'Микросервисная архитектура: основы и преимущества',
    'Микросервисы инкапсулируют хранение и извлечение данных, предоставляя данные через четко определенные интерфейсы. БД скрыты в пределах контура службы.',
    'https://example.com/images/microservices.jpg',
    'Внутренний источник / авторский материал',
    jsonb_build_array(
        -- Введение
            jsonb_build_object(
                    'type', 'paragraph',
                    'content', 'Микросервисы инкапсулируют хранение и извлечение данных, предоставляя данные через четко определенные интерфейсы. Поэтому БД скрыты в пределах контура службы.'
            ),
        -- Особенности
            jsonb_build_object('type', 'heading', 'content', 'Особенности:'),
            jsonb_build_object(
                    'type', 'list',
                    'items', jsonb_build_array(
                            'Независимая развертываемость - возможность вносить изменения в службу и развертывать в производственной среде без других служб. Для обеспечения гарантии независимого развертывания службы должны быть слабо сопряжены. Мы должны иметь возможность поднять службу без необходимости менять что-либо еще. Это означает, что нам нужны четко определенные и стабильные контракты между службами.',
                            'Нейтральность к технологиям.',
                            'Сервисы следует моделировать вокруг бизнес домена',
                            'Владение собственными данными. Микрослужбы не должны использовать данные совместно. Если одна служба хочет обратиться к данным, хранящимся в другой службе, то она должна запросить у этой службы необходимые данные. Такой подход дает службам право решать, какие данные являются совместными, а какие скрытыми. Это позволяет службам отображать детали внутренней имплементации, которые могут измениться по различным причинам, в более четкий публичный контракт, обеспечивающий стабильный интерфейс между службами. Наличие стабильных интерфейсов между службами имеет большое значение, если мы хотим независимой развертываемости.'
                             )
            ),
        -- Преимущества
            jsonb_build_object('type', 'heading', 'content', 'Преимущества:'),
            jsonb_build_object(
                    'type', 'paragraph',
                    'content', 'Независимая природа развертываний открывает новые модели повышения масштаба и работоспособности систем, так же позволяет комбинировать и сочетать технологии.'
            )
    ),
    1
