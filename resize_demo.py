import numpy as np
import matplotlib.pyplot as plt

# 生成一个3x3x3的原始三维矩阵
original_matrix = np.array([[[1, 2, 3],
                             [4, 5, 6],
                             [7, 8, 9]],
                            [[10, 11, 12],
                             [13, 14, 15],
                             [16, 17, 18]],
                            [[19, 20, 21],
                             [22, 23, 24],
                             [25, 26, 27]]])

print("原始3x3x3矩阵:")
print(original_matrix)

# 调整矩阵大小为2x2x5
resized_matrix = np.resize(original_matrix, (3, 1, 9))

# 进行transpose操作（转置）
transposed_matrix = np.transpose(original_matrix,(0,1))

print("\n转置后的矩阵 (transpose):")
print(transposed_matrix)

print("\n调整后的矩阵 (2x2x5):")
print(resized_matrix)

# 可视化三维矩阵的每一层
fig, ax = plt.subplots(2, 3, figsize=(12, 8))

# 原始矩阵的可视化
for i in range(3):
    ax[0, i].imshow(original_matrix[i], cmap='viridis', interpolation='none')
    ax[0, i].set_title(f"原始矩阵 第{i+1}层")

# 调整后矩阵的可视化
for i in range(2):
    ax[1, i].imshow(resized_matrix[i], cmap='viridis', interpolation='none')
    ax[1, i].set_title(f"调整后矩阵 第{i+1}层")

# 隐藏空白的子图
ax[1, 2].axis('off')

plt.show()
