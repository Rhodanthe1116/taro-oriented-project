# taro-oriented-project
## log
###### tags: `程式專案`,`紫芋導向`,`start travel`
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

--- 
> 開發方向
> - 
> - 萃取觀察者模式
> - 使用提醒(不要再提醒利用SharedPreference)
> - Invoker實例是否提升層級
> - 整理程式碼 註解
> - 搜尋清單數量問題
> - 日期按下即時更新
> - 搜尋列表加入出團狀態
> - 訂購系統引入時間概念
>     - 搜尋端如果超過時間就不能訂
>     - 使用者訂單(列表)會顯示
>       「即將開始、進行中、已結束」
>     - 訂單若在即將開始前?天無法修改抑或取消
> - 主畫面功能
> - 
> - Auth 讀寫資料庫設定

### alpha-1.6
> 版本資訊：
> -
> 
> **概述**
> > 新增彩蛋~~~~~~~~
> > 移植地區選擇至搜尋欄並完整化
> > 實作訂購、取消訂單
> 
> **類別更動**
> - 新增PlaceSuggestion
> - 新增PlaceCounselor
> - 新增CodeMapping以隔離Command跟Switch地區的複雜情形
> - 新增ExtirpateOrderCommand
> - 微調TravelAdapter讓無結果時顯示Alert
> - 修改AddOrderCommand
>     
> **GUI處理類別更動**
> - (新增)**彩蛋**
> - (修改)**搜尋frag**:篩選地區移植到搜尋欄、日期鈕長按取消
> - (修改)**訂購acti**:調整一些限制、例外
> - (新增)**動作完成anim**:成功訂購、移除以及~~修改~~
> 
> **GUI資料更動**
> - 加入彩蛋drawable
> - 加入seekbar_thumb