// mongo-init.js
db = db.getSiblingDB('admin');
db.auth('admin', 'admin_password');

// stock 데이터베이스 생성
db = db.getSiblingDB('stock');

// stock 데이터베이스에 대한 권한 설정
db.createUser({
    user: 'min9805',
    pwd: 'backend',
    roles: [
        {
            role: 'readWrite',
            db: 'stock'
        }
    ]
});

db = db.getSiblingDB('stock');
db.createCollection('stocks');