--
-- Database Schema:  caeschema
-- Automatically generated sql script for the service FAQ-Service, created by the CAE.
-- --------------------------------------------------------

--
-- Table structure for table faq.
--
CREATE TABLE caeschema.faq (
  question VARCHAR(255) ,
  id INT ,
  answer VARCHAR(255) ,
CONSTRAINT id_PK PRIMARY KEY (id)
 
);



