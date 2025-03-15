.PHONY: test_obj_exe_run test_obj_cuda_run clean

GCC = g++
NVCC = nvcc
CUDA_FLAGS = -arch=sm_86

test_obj_exe_run:
					$(GCC) -c Driver.cpp Test.cpp Run.cpp
					$(GCC) -o Driver Driver.o
					$(GCC) -o Test Test.o
					$(GCC) -o Run Run.o
					./Driver

test_obj_cuda_run: 
					$(NVCC) $(CUDA_FLAGS) -o DriverCuda Driver.cu
					./DriverCuda

clean:
		rm -f *.exe *.cu *.o Driver DriverCuda Test Run