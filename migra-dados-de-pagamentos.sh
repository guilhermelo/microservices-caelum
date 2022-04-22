docker-compose exec -T mysql.monolito mysqldump -u root -p caelum123 --opt eats_pagamento | docker-compose exec -T mysql.pagamento mysql -u pagamento -p pagamento123 eats_pagamento
