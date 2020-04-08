# taro-oriented-project
taro-oriented project
## log
### alpha-1.1
> 版本資訊：
> -
> 
> **概述**
> 
> >將處理使用者的資訊完全交由資料庫處理
> >抽出**GUI處理類別**所有跟資料庫有關的語法並簡化使用者類別的責任
> >**UseInn**從萬能本地使用者管理員變成**只處理使用者認證機制**
> 
> **類別新增**
> - 新增package:util來裝一些設計模式的類別:命令模式、觀察者模式
> - 新增簡單的Exception：CommandException
> - 命令模式用來處理操作database資料的介面
> 
> | DatabaseInvoker Ⓒ| DBCommand Ⓐ| HanWen Ⓒ|
> | -------- | -------- | -------- |
> | 命令執行的管理者     | 命令(abstract)     | 命令執行者     |
> >值得注意的是只有Invoker有公開的方法可以調用，其餘預設是package private
> - 目前具體的命令類別有：
>    - 設定使用者 SetUserCommand
>    - 取得使用者 GetUserCommand (另外也是**具體的觀察主體concrete subject**)
>    - 新增訂單 AddOrderCommand
> - 操作展示：
>  ```java
>  DatabaseInvoker invoker = new DatabaseInvoker();
>  invoker.addCommand(new AddOrderCommand(new HanWen(),UID,new Order(travel)));
> invoker.assignCommand();
> ```
> - 觀察者模式用來處理database資料更新
> 
> | DBDataObserver Ⓘ | ~~Subject~~(目前並無介面) |
> | -------- | -------- |
> | 觀察者     | 觀察主體     |
> - 目前只有一個具體的觀察主體：GetUserCommand
> - 操作展示
> ```java
> NameDBObserver dbObserver = new NameDBObserver(userName);
> command.attach(dbObserver,NAME);
> ```
> **GUI處理類別更動**
> - ~~import Firebase.*~~ 移除對Firebase類別的依賴
> - 部分View會透過會傳參考物件給觀察者更新內容
>     - UserFragment中的TextView userName
>     - ListFragment中的recylerView以及getActivity()
> 
> **GUI資料更動**
> - 無
>
--- 
> 開發方向
> - 
> - 萃取觀察者模式
> - Invoker實例是否提升層級
> - 觀察主體的介面
> - Concrete Command設計並開發
> - Travel資料升級
>      - 已訂人數
>      
> - Travel資料交由database處理並修改**travel_data package**
> - Order資料升級
>     - 成人訂購人數
>     - 孩童訂購人數
>     - 嬰兒訂購人數
>     - 訂單UID
> - GUI處理類別以及資料更新至4/6討論內容
>     - (修改)**搜尋frag**：具排序、各種篩選的選項、拿掉Alert
>     - (修改)**使用者frag**:多加一張「登入後layout」以及一張「訂單詳資layout」
>     - (移植)**清單frag**:移到使用者frag中
>     - (新增)**Travel詳資acti**:下方有訂購鈕
>     - (新增)**訂購acti**:訂購頁面
>     - (移植)**Travel處理者acti**:移到搜尋frag中

