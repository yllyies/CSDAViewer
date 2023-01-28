import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
from sklearn.preprocessing import StandardScaler
from sklearn.cluster import KMeans

if __name__ == '__main__':
    mos = pd.read_csv('C:/Users/michaeli/Downloads/bigdata.csv')
    mos.info()
    X = mos[["power"]]
    s = StandardScaler()
    X_scale = s.fit_transform(X)
    X_scale = pd.DataFrame(X_scale, columns=X.columns, index=X.index)
    X_scale.head()

    # 定义候选的K值。
    scope = range(1, 1)
    # 定义SSE列表，用来存放不同K值下的SSE。
    sse = []
    for k in scope:
        kmeans = KMeans(n_clusters=k)
        kmeans.fit(X_scale)
        sse.append(kmeans.inertia_)
    plt.xticks(scope)
    sns.lineplot(x=scope, y=sse, markers="o")

    kmeans = KMeans(n_clusters=3)
    kmeans.fit(X_scale)
    # 获取每个样本所属的簇。标签的数值对应所属簇的索引。
    print("标签：", kmeans.labels_)

    mos['kmeans.labels_'] = kmeans.labels_
    print(mos)

    fig =plt.figure(figsize=(4,4), dpi=100)
    color = ["r", "g", "b"]
    for i in range(3):
        d = mos[kmeans.labels_ == i]
        plt.scatter(d.power, d.date,color=color[i], label=f"功率{i}")
        plt.legend()
        plt.rcParams['font.family']='SimHei'
        plt.rcParams['axes.unicode_minus']='False'
    mos.to_csv('C:/Users/michaeli/Downloads/SoftwarePackage/shuju.csv') #将打了标签的数据保存
    print('完成')