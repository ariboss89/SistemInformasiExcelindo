# Host: localhost  (Version: 5.5.8)
# Date: 2020-01-06 23:58:19
# Generator: MySQL-Front 5.3  (Build 4.81)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "admin"
#

DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `nama` varchar(255) DEFAULT NULL,
  `username` varchar(20) NOT NULL DEFAULT '',
  `alamat` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `konfirmasi` varchar(255) DEFAULT NULL,
  `kategori` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Data for table "admin"
#

INSERT INTO `admin` VALUES ('Ari Ramadhan','ariboss89','Jl. Gudang Minyak','123456','123456','SuperAdmin'),('Siti Chairani','siti89','Jl. Gudang Minyak No. 28','123456','123456','Admin');

#
# Structure for table "barang"
#

DROP TABLE IF EXISTS `barang`;
CREATE TABLE `barang` (
  `Id` varchar(11) NOT NULL DEFAULT '',
  `nama` varchar(255) DEFAULT NULL,
  `kategori` varchar(255) DEFAULT NULL,
  `hargabeli` int(11) DEFAULT NULL,
  `hargajual` int(11) DEFAULT NULL,
  `stok` int(11) DEFAULT NULL,
  `minstok` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Data for table "barang"
#

INSERT INTO `barang` VALUES ('B0000001','Tv 21 inch','ELEKTRONIK',1200000,1400000,18,10);

#
# Structure for table "barangmasuk"
#

DROP TABLE IF EXISTS `barangmasuk`;
CREATE TABLE `barangmasuk` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `nama` varchar(50) DEFAULT NULL,
  `tanggal` date DEFAULT NULL,
  `kategori` varchar(50) DEFAULT NULL,
  `stok` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

#
# Data for table "barangmasuk"
#

INSERT INTO `barangmasuk` VALUES (2,'Tv 21 inch','2020-01-04','ELEKTRONIK',0),(3,'Tv 21 inch','2020-01-05','ELEKTRONIK',26);

#
# Structure for table "detailpembelian"
#

DROP TABLE IF EXISTS `detailpembelian`;
CREATE TABLE `detailpembelian` (
  `iddetail` varchar(11) NOT NULL DEFAULT '',
  `idpembelian` varchar(11) DEFAULT NULL,
  `nama` varchar(25) DEFAULT NULL,
  `jumlah` int(11) DEFAULT NULL,
  PRIMARY KEY (`iddetail`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

#
# Data for table "detailpembelian"
#

INSERT INTO `detailpembelian` VALUES ('DTB001','NP001','Tv 21 inch',10);

#
# Structure for table "detailtransaksi"
#

DROP TABLE IF EXISTS `detailtransaksi`;
CREATE TABLE `detailtransaksi` (
  `iddetail` varchar(11) NOT NULL DEFAULT '',
  `idtransaksi` varchar(11) DEFAULT NULL,
  `nama` varchar(25) DEFAULT NULL,
  `harga` int(11) DEFAULT NULL,
  `jumlah` int(11) DEFAULT NULL,
  `total` int(11) DEFAULT NULL,
  PRIMARY KEY (`iddetail`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Data for table "detailtransaksi"
#

INSERT INTO `detailtransaksi` VALUES ('DT001','N001','Tv 21 inch',1200000,1,1200000),('DT002','N002','Tv 21 inch',1200000,1,1200000);

#
# Structure for table "kategori"
#

DROP TABLE IF EXISTS `kategori`;
CREATE TABLE `kategori` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `kategori` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

#
# Data for table "kategori"
#

INSERT INTO `kategori` VALUES (1,'qucingi');

#
# Structure for table "pelanggan"
#

DROP TABLE IF EXISTS `pelanggan`;
CREATE TABLE `pelanggan` (
  `Id` varchar(11) NOT NULL DEFAULT '',
  `nama` varchar(255) DEFAULT NULL,
  `alamat` varchar(255) DEFAULT NULL,
  `kontak` varchar(12) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Data for table "pelanggan"
#

INSERT INTO `pelanggan` VALUES ('P0000001','Siti Chairani','Jl. Wertyonokiolionima','087687786554'),('P0000002','qqoi','qqqqq','90');

#
# Structure for table "pembelian"
#

DROP TABLE IF EXISTS `pembelian`;
CREATE TABLE `pembelian` (
  `idpembelian` varchar(11) NOT NULL DEFAULT '',
  `tanggal` date DEFAULT NULL,
  `jumlah` int(11) DEFAULT NULL,
  `supplier` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`idpembelian`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

#
# Data for table "pembelian"
#

INSERT INTO `pembelian` VALUES ('NP001','2020-01-06',10,'Cv. Tuah Jaya Perdana');

#
# Structure for table "stok"
#

DROP TABLE IF EXISTS `stok`;
CREATE TABLE `stok` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `tanggal` date DEFAULT NULL,
  `nama` varchar(25) DEFAULT NULL,
  `kategori` varchar(25) DEFAULT NULL,
  `hargabeli` int(11) DEFAULT NULL,
  `hargajual` int(11) DEFAULT NULL,
  `stok` int(11) DEFAULT NULL,
  `minstok` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Data for table "stok"
#


#
# Structure for table "supplier"
#

DROP TABLE IF EXISTS `supplier`;
CREATE TABLE `supplier` (
  `Id` varchar(11) NOT NULL DEFAULT '',
  `nama` varchar(255) DEFAULT NULL,
  `alamat` varchar(255) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `telepon` varchar(12) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Data for table "supplier"
#

INSERT INTO `supplier` VALUES ('S001','Cv. Tuah Jaya Perdana','Jl. Ganet Blok Catur Manise','tjperdana@yahoo.com','081278765444');

#
# Structure for table "transaksi"
#

DROP TABLE IF EXISTS `transaksi`;
CREATE TABLE `transaksi` (
  `idtransaksi` varchar(11) NOT NULL DEFAULT '',
  `tanggal` date DEFAULT NULL,
  `jumlah` int(11) DEFAULT NULL,
  `total` int(11) DEFAULT NULL,
  `bayar` int(11) DEFAULT NULL,
  `kembalian` int(11) DEFAULT NULL,
  `pelanggan` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`idtransaksi`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Data for table "transaksi"
#

INSERT INTO `transaksi` VALUES ('N001','2020-01-05',1,1200000,10000000,8800000,'Siti Chairani'),('N002','2020-01-05',1,1200000,1250000,50000,'Siti Chairani');
