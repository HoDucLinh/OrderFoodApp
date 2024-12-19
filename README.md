# Ứng dụng Đặt Đồ Ăn Trực Tuyến (OrderFoodApp)

Ứng dụng này kết nối người dùng với nhà hàng, cho phép đặt đồ ăn trực tuyến một cách tiện lợi.

## Cấu trúc Mã Nguồn

Dự án được tổ chức theo cấu trúc rõ ràng, tuân theo mô hình MVVM (Model-View-ViewModel).

*   **`adapter`**: Chứa các lớp adapter, có nhiệm vụ liên kết dữ liệu với giao diện người dùng (UI).
*   **`data`**: Xử lý dữ liệu của ứng dụng.
    *   **`api`**: Xử lý các yêu cầu API, bao gồm các chức năng liên quan đến xác thực và thanh toán.
    *   **`dao`**: Quản lý các truy vấn và tương tác với cơ sở dữ liệu.
    *   **`model`**: Chứa các mô hình dữ liệu và các tiện ích hỗ trợ như hộp thoại xác nhận, quản lý cơ sở dữ liệu và vị trí.
*   **`view`**: Chứa các lớp giao diện hiển thị cho người dùng.
*   **`viewmodel`**: Xử lý logic trung gian giữa giao diện và dữ liệu, tuân theo mô hình MVVM.

## Cách Chạy Ứng Dụng

Để chạy ứng dụng, bạn có thể làm theo các bước sau:

1.  **Mở dự án trong Android Studio:** Khởi động Android Studio.
2.  **Clone project:** Sử dụng lệnh sau trong terminal hoặc Git Bash:

    ```bash
    git clone <path_to_repository>
    ```

    Thay `<path_to_repository>` bằng đường dẫn đến kho lưu trữ Git.
3.  **Chọn thiết bị:** Chọn một thiết bị ảo (emulator) hoặc thiết bị thực đã kết nối với máy tính để chạy ứng dụng.
4.  **Chạy ứng dụng:** Nhấn nút "Run" (biểu tượng Play) trên thanh công cụ của Android Studio hoặc sử dụng tổ hợp phím `Shift + F10`.

Ứng dụng sẽ được biên dịch, cài đặt và chạy trên thiết bị bạn đã chọn.

## Tài Khoản Mẫu

Dưới đây là một số tài khoản mẫu để bạn có thể thử nghiệm ứng dụng:

*   **Tài khoản Admin:** `admin@gmail.com`
*   **Tài khoản Nhà hàng:** `restaurant@gmail.com`
*   **Tài khoản Khách hàng:** `customer@gmail.com`
*   
> [!WARNING]
> Chú ý quan trọng: Vui lòng KHÔNG đẩy (push) bất kỳ thay đổi nào lên nhánh `main`.
