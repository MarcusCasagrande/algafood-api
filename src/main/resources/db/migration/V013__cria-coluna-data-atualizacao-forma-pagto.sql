alter table forma_pagamento add data_atualizacao datetime null; -- será not null, mas inciialmente poe null pois é uma coluna numa tabela ja exisente que tenta por um valor padrao de data 000000 que nao existe
update forma_pagamento set data_atualizacao = utc_timestamp;
alter table forma_pagamento modify data_atualizacao datetime not null;