--
-- Table structure for table `key_store`
--

CREATE TABLE `key_store` (
  `name` varchar(50) NOT NULL,
  `licence_key` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for table `key_store`
--
ALTER TABLE `key_store`
  ADD PRIMARY KEY (`name`);
COMMIT;

