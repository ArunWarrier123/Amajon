package com.warrier.amajon.services;


import com.warrier.amajon.exceptions.MyApiException;
import com.warrier.amajon.exceptions.ResourceNotFoundException;
import com.warrier.amajon.models.Category;
import com.warrier.amajon.models.Product;
import com.warrier.amajon.payload.ProductDTO;
import com.warrier.amajon.services.FileService;
import com.warrier.amajon.payload.ProductResponse;
import com.warrier.amajon.repositories.CategoryRepository;
import com.warrier.amajon.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO addProduct(ProductDTO product, Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category" , "categoryId" , categoryId));
        //Checking if it already exists
        Product presentInDB = productRepository.findByProductNameIgnoreCase(product.getProductName());
        if(presentInDB != null) {
            throw new MyApiException("Product already exists!");
        }
        Product existingProduct = modelMapper.map(product, Product.class);
        System.out.println("ASDFASD");
        existingProduct.setProductCategory(category);
        existingProduct.setSpecialPrice(
                existingProduct.getProductPrice() - (existingProduct.getDiscount() * 0.01 * existingProduct.getProductPrice())
        );
        Product productAdded = productRepository.save(existingProduct);

        return modelMapper.map(productAdded, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndSortOrder = sortOrder.equals("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
                : Sort.by(Sort.Direction.DESC, sortBy);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize,  sortByAndSortOrder);
        Page<Product> productPage = productRepository.findAll(pageRequest);
        List<Product> products = productPage.getContent();
        if(products.isEmpty()) {
            throw new MyApiException("No Products found!");
        }
        ProductResponse productResponse = new ProductResponse();
        List<ProductDTO> productDTOList = products.stream().map(p -> modelMapper.map(p, ProductDTO.class)).toList();
        productResponse.setContent(productDTOList);
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setLastPage(productPage.isLast());
        productResponse.setTotalElements((int)productPage.getTotalElements());
        return productResponse;
    }

    @Override
    public ResponseEntity<ProductResponse> getProductsByCategory(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category" , "categoryId" , categoryId));

        List<Product> productsByCategory = productRepository.findByProductCategory(category);

        if(productsByCategory.isEmpty()) {
            throw new MyApiException("No Products found!");
        }
        List<ProductDTO> productDTOList = productsByCategory.stream().
                map(p -> modelMapper.map(p, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOList);
        return ResponseEntity.ok(productResponse);
    }

    @Override
    public ResponseEntity<ProductResponse> getProductsBykeyword(String keyword) {
        List<Product> productsByKeyword = productRepository.findByProductNameLikeIgnoreCase("%" + keyword + "%");
        if(productsByKeyword.isEmpty()) {
            throw new MyApiException("No Products found!");
        }
        List<ProductDTO> productDTOList = productsByKeyword.stream().map(p -> modelMapper.map(p, ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOList);
        return ResponseEntity.ok(productResponse);
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(productId).orElseThrow(
                () -> {
                    return new ResourceNotFoundException("Product", "product", Math.toIntExact(productId));
                }
        );

        existingProduct.setProductName(productDTO.getProductName());
        existingProduct.setProductDescription(productDTO.getProductDescription());
        existingProduct.setProductPrice(productDTO.getProductPrice());
        existingProduct.setDiscount(productDTO.getDiscount());
        existingProduct.setProductQuantity(productDTO.getProductQuantity());
        existingProduct.setSpecialPrice(
                productDTO.getProductPrice() - (productDTO.getDiscount() * 0.01 * productDTO.getProductPrice())
        );

        productRepository.save(existingProduct);
        return modelMapper.map(existingProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product existingProduct = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product" ,  "product", Math.toIntExact(productId)));

        productRepository.delete(existingProduct);
        return modelMapper.map(existingProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO uploadProductImage(Long productId, MultipartFile image) throws IOException {
        Product existingProduct = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product" ,  "product", Math.toIntExact(productId)));
        String imageName = fileService.uploadImageToServer(image,path);
        existingProduct.setProductImage(imageName);
        productRepository.save(existingProduct);
        return modelMapper.map(existingProduct, ProductDTO.class);
    }




}
