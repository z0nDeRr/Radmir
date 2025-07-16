package ru.Radmir.tgBot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.Radmir.tgBot.model.*;
import ru.Radmir.tgBot.repository.*;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@SpringBootTest
class FillingTests {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ClientOrderRepository clientOrderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Test
    void fillTestData() {
        createClients();
        createCategoriesAndProducts();
    }

    private void createClients() {
        saveClient(1L, "Иванов Иван Иванович", "+79131112233", "Улица Ленина, дом 42, квартира 15, г. Новосибирск");
        saveClient(2L, "Петров Петр Петрович", "+79132223344", "Улица Правды, дом 22, квартира 1, г. Краснопарск");
        saveClient(3L, "Сидоров Сидр Сидорович", "+79133334455", "Улица Кирова, дом 19, квартира 45, г. Найкон");
    }

    private void saveClient(Long externalId, String fullName, String phoneNumber, String address) {
        Client client = new Client();
        client.setExternalId(externalId);
        client.setFullName(fullName);
        client.setPhoneNumber(phoneNumber);
        client.setAddress(address);
        clientRepository.save(client);
    }

    private void createCategoriesAndProducts() {
        // Создаем основные категории
        Category pizza = saveCategory("Пицца", null);
        Category rolls = saveCategory("Роллы", null);
        Category burgers = saveCategory("Бургеры", null);
        Category drinks = saveCategory("Напитки", null);

        // Подкатегории для Роллов
        Category classicRolls = saveCategory("Классические роллы", rolls);
        Category bakedRolls = saveCategory("Запеченные роллы", rolls);
        Category sweetRolls = saveCategory("Сладкие роллы", rolls);
        Category rollSets = saveCategory("Наборы", rolls);

        // Подкатегории для Бургеров
        Category classicBurgers = saveCategory("Классические бургеры", burgers);
        Category spicyBurgers = saveCategory("Острые бургеры", burgers);

        // Подкатегории для Напитков
        Category soda = saveCategory("Газированные напитки", drinks);
        Category energy = saveCategory("Энергетические напитки", drinks);
        Category juices = saveCategory("Соки", drinks);
        Category otherDrinks = saveCategory("Другие", drinks);

        // Создаем продукты для каждой подкатегории (минимум 3 на подкатегорию)
        createProductsForCategory(
                classicRolls,
                "Филадельфия", "Классические роллы с лососем и сливочным сыром", "350.0",
                "Калифорния", "Роллы с крабом и авокадо", "320.0",
                "Дракон", "Роллы с угрем и огурцом", "380.0"
        );

        createProductsForCategory(
                bakedRolls,
                "Запеченный с лососем", "Запеченные роллы с лососем под сыром", "420.0",
                "Запеченный с креветкой", "Запеченные роллы с креветкой", "450.0",
                "Запеченный с угрем", "Запеченные роллы с угрем", "480.0"
        );

        // Аналогично для остальных категорий...
        createProductsForCategory(
                classicBurgers,
                "Классик", "Говяжья котлета, сыр, салат, соус", "250.0",
                "Чизбургер", "Говяжья котлета, сыр чеддер, лук, соус", "280.0",
                "Двойной", "Две говяжьи котлеты, сыр, бекон", "350.0"
        );
        createProductsForCategory(
                soda,
                "Кола", "Газированный напиток 0.5л", "100.0",
                "Фанта", "Апельсиновый газированный напиток 0.5л", "100.0",
                "Спрайт", "Лимонный газированный напиток 0.5л", "100.0"
        );
    }

    private Category saveCategory(String name, Category parent) {
        Category category = new Category();
        category.setName(name);
        category.setParent(parent);
        return categoryRepository.save(category);
    }

    private void createProductsForCategory(Category category, String... productData) {
        for (int i = 0; i < productData.length; i += 3) {
            Product product = new Product();
            product.setCategory(category);
            product.setName(productData[i]);
            product.setDescription(productData[i+1]);
            product.setPrice(Double.parseDouble(productData[i+2].toString()));
            productRepository.save(product);
        }
    }
}