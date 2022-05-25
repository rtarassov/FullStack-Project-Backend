package tarassov.project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tarassov.project.dto.ProductRequest;
import tarassov.project.dto.ProductStorageRequest;
import tarassov.project.model.Product;
import tarassov.project.model.ProductType;
import tarassov.project.repository.ProductRepository;
import tarassov.project.repository.StorageRepository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final StorageRepository storageRepository;
    private final ServiceValidations serviceValidations;

    public ProductService(ProductRepository productRepository, StorageRepository storageRepository, ServiceValidations serviceValidations) {
        this.productRepository = productRepository;
        this.storageRepository = storageRepository;
        this.serviceValidations = serviceValidations;
    }

    // Doesn't work
    public void addProductToStorage(ProductStorageRequest productStorageRequest) {
        try {
            var storageObject = storageRepository.getById(productStorageRequest.getStorageId());
            // TODO:
            // Need to rethink the database, because here I would need to have a List<Product> in Storage,
            // But it messes up the tables.
            // storageObject.getProduct().add(productObject);
            storageRepository.save(storageObject);
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public Long saveProductToDB(ProductRequest entity) {
        log.info("Entity to save: [{}]", entity);

        try {
            var storageObject = storageRepository.getById(entity.getStorageId());
            var productObject = new Product();

            if (serviceValidations.checkForCharacters(entity.getName())) {
                productObject.setName(entity.getName());
            } else {
                throw new IllegalArgumentException("Name is not at least 3 characters or contains symbols.");
            }
            productObject.setSerialNumber(entity.getSerialNumber());
            productObject.setDescription(entity.getDescription());
            productObject.setProductType(ProductType.valueOf(entity.getProductType()));
            productObject.setPrice(entity.getPrice());
            productObject.setStorage(storageObject);
            productObject.setPurchaseDate(Date.valueOf(entity.getPurchaseDate()));

            productRepository.save(productObject);
            return productObject.getId();
        } catch (Exception e) {
            log.info(e.toString());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public boolean deleteProductById(Long id) {
        log.info("Trying to delete Product by id: [{}]", id);

        boolean result = false;
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            result = true;
        }
        return result;
    }

    public Optional<Product> findProductById(Long id) {
        log.info("Trying to find product by id: [{}]", id);
        var product = productRepository.findById(id);

        log.info("Found product from database: [{}]", product);
        return product;
    }

    public List<Product> findAllProducts() {
        log.info("Trying to find all products from database");
        var products = productRepository.findAll();
        log.info("Products found: " + products);
        return products;
    }
}
