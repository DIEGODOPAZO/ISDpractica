-- ----------------------------------------------------------------------------
-- Model
-------------------------------------------------------------------------------
DROP TABLE Sale;
DROP TABLE Partido;
-- --------------------------------- Partido ------------------------------------
CREATE TABLE Partido (
    partidoId BIGINT NOT NULL AUTO_INCREMENT,
    rival VARCHAR(255) COLLATE latin1_bin NOT NULL,
    entradasVendidas INT NOT NULL,
    entradasIniciales INT NOT NULL,
    price FLOAT NOT NULL,
    creationDate DATETIME NOT NULL,
    datep DATETIME NOT NULL,
    PRIMARY KEY (partidoId), -- Definición de la clave primaria
    CHECK (entradasVendidas >= 0), -- Restricción de verificación
    CHECK (price >= 0)
) ENGINE = InnoDB;

CREATE TABLE Sale (
    saleId BIGINT NOT NULL AUTO_INCREMENT,
    partidoId BIGINT NOT NULL,
    userMail VARCHAR(40) COLLATE latin1_bin NOT NULL,
    creditCardNumber VARCHAR(16),
    saleDate DATETIME NOT NULL,
    amount INT NOT NULL,
    delivered BOOLEAN NOT NULL DEFAULT FALSE, -- Nuevo campo 'delivered'
    PRIMARY KEY (saleId),
    FOREIGN KEY (partidoId) REFERENCES Partido(partidoId) ON DELETE CASCADE
) ENGINE = InnoDB;

