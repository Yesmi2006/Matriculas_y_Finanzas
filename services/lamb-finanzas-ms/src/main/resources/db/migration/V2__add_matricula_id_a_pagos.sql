ALTER TABLE pagos ADD COLUMN matricula_id BIGINT;
ALTER TABLE pagos ADD CONSTRAINT uk_pagos_matricula_id UNIQUE (matricula_id);
ALTER TABLE pagos ALTER COLUMN metodo DROP NOT NULL;
