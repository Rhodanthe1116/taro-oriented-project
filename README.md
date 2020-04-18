# taro-oriented-project
## log
###### tags: `程式專案`,`紫芋導向`,`start travel`
### alpha-1.8
> 版本資訊：
> -
> 
> **概述**
> > 訂購系統引入時間、出團狀態系統
> 
> **類別更動**
> - 新增TravelStateOffice來判斷travel的狀態
>     - 搜尋端如果超過時間就不能訂
>     - 使用者訂單(列表)會顯示
>       「即將開始、進行中、已結束」
>     - 訂單若在即將開始前?天無法修改抑或取消(第四種狀態)
>     
> **GUI處理類別更動**
> - (微調)**搜尋frag**:顯示出團、時間狀態
> - (微調)**使用者frag**:顯示時間狀態
> - (微調)**旅遊資訊acti**:按鈕隨狀態更新
> - (微調)**訂單資訊acti**:按鈕隨狀態更新
> - 訂購畫面新增訂購提醒dialog
> 
> **GUI資料更動**
> - 微調旅遊資訊acti
> - 微調訂單資訊acti
> - 新增dialog_remind

--- 
> 開發方向
> - 
> - 萃取觀察者模式
> - Invoker實例是否提升層級
> - 整理程式碼 註解
> - 搜尋清單數量問題
> - orders中的travel要跟travels的同步
> - 日期按下即時更新
> - 主畫面frag功能調整
> - 使用者fragGUI優化
> - Auth 讀寫資料庫設定

### alpha-1.7
> 版本資訊：
> -
> 
> **概述**
> > 修改訂單實作
> > 自定義Exception的處理
> 
> **類別更動**
> - 完成修改訂單的功能
> - 新增ReviseOrderCommand
> - Order微調以方便Command編寫
> - 補完小地區的選擇
> - 重構 LookForCodesCommand
> - CommandException的處理
>     - HW_NULL
>     - INPUT_INVALID
>     - NO_RESULT
>     
> **GUI處理類別更動**
> - (微調)**搜尋frag**:改排序到右上角
> 
> **GUI資料更動**
> - 加入ReviseOrder
